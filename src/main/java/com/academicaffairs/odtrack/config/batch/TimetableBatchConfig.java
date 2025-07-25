package com.academicaffairs.odtrack.config.batch;

import com.academicaffairs.odtrack.entity.Timetable;
import com.academicaffairs.odtrack.entity.User;
import com.academicaffairs.odtrack.payload.TimetableCsvDto;
import com.academicaffairs.odtrack.repository.TimetableRepository;
import com.academicaffairs.odtrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class TimetableBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserRepository userRepository;
    private final TimetableRepository timetableRepository;

    @Bean
    public FlatFileItemReader<TimetableCsvDto> timetableCsvReader() {
        return new FlatFileItemReaderBuilder<TimetableCsvDto>()
                .name("timetableCsvReader")
                .resource(new FileSystemResource("uploads/timetable.csv")) // TODO: Make path configurable
                .delimited()
                .names(new String[]{"staffEmail", "date", "period", "year", "hall", "subject", "dayOfWeek", "startTime", "endTime"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(TimetableCsvDto.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<TimetableCsvDto, Timetable> timetableProcessor() {
        return csvDto -> {
            User staff = userRepository.findByEmail(csvDto.getStaffEmail())
                    .orElseThrow(() -> new RuntimeException("Staff not found: " + csvDto.getStaffEmail()));

            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate date = LocalDate.parse(csvDto.getDate(), dateFormatter);

            return Timetable.builder()
                    .staff(staff)
                    .date(date)
                    .period(Integer.parseInt(csvDto.getPeriod()))
                    .year(Integer.parseInt(csvDto.getYear()))
                    .hall(csvDto.getHall())
                    .subject(csvDto.getSubject())
                    .dayOfWeek(csvDto.getDayOfWeek())
                    .startTime(csvDto.getStartTime())
                    .endTime(csvDto.getEndTime())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        };
    }

    @Bean
    public ItemWriter<Timetable> timetableWriter() {
        return timetables -> timetableRepository.saveAll(timetables);
    }

    @Bean
    public Step importTimetableStep(FlatFileItemReader<TimetableCsvDto> reader, ItemProcessor<TimetableCsvDto, Timetable> processor, ItemWriter<Timetable> writer) {
        return new StepBuilder("importTimetableStep", jobRepository)
                .<TimetableCsvDto, Timetable>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importTimetableJob(Step importTimetableStep) {
        return new JobBuilder("importTimetableJob", jobRepository)
                .start(importTimetableStep)
                .build();
    }
}
