package com.jobboard.job_board;


import com.jobboard.job_board.company.Company;
import com.jobboard.job_board.company.CompanyRepo;
import com.jobboard.job_board.job.Job;
import com.jobboard.job_board.job.JobRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setExtractBareNamePropertyMethods;


@DataJpaTest
public class JobRepoTest {

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @BeforeEach
        // runs before EVERY test — sets up fresh data
    void setup() {

        // create company first — Job needs a company
        Company tcs = new Company();
        tcs.setName("TCS");
        tcs.setLocation("Pune");
        companyRepo.save(tcs);

        Job job1 = Job.builder()
                .title("Backend Developer")
                .description("Spring Boot role")
                .location("Pune")
                .jobtype("Full-time")
                .salary(new BigDecimal("800000"))
                .company(tcs)
                .build();

        Job job2 = Job.builder()
                .title("Frontend Developer")
                .description("React role")
                .location("Bangalore")
                .jobtype("Remote")
                .salary(new BigDecimal("600000"))
                .company(tcs)
                .build();

        Job job3 = Job.builder()
                .title("DevOps Engineer")
                .description("AWS and Docker")
                .location("Pune")
                .jobtype("Full-time")
                .salary(new BigDecimal("900000"))
                .company(tcs)
                .build();

        jobRepo.save(job1);
        jobRepo.save(job2);
        jobRepo.save(job3);
    }

    @Test
    void testfindByLocation() {

        Page<Job> result = jobRepo.findByLocation("Bangalore",PageRequest.of(0,5));

        System.out.println("Total jobs: "+result.getTotalElements());
        System.out.println("Job title: "+result.getContent().get(0).getTitle());

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Frontend Developer");
    }

    @Test
    void tesFindBySalaryBetween(){
        List<Job> result = jobRepo.findBySalaryBetween(
                new BigDecimal("500000"),
                new BigDecimal("900000")
        );

        System.out.println("Salary: "+result.getFirst().getSalary());

        assertThat(result).hasSize(3);

    }

//    @Test
//    void testFindTitleContainingIgnoreCase(){
//        List<Job> result=jobRepo.findByTitleContainingIgnoreCase("developer");
//
//        System.out.println("Title belonging: "+result.getLast().getTitle());
//
//        assertThat(result).hasSize(2);
//    }

    @Test
    void testfindJobsAboveSalary(){
        List<Job> result=jobRepo.findJobsAboveSalary(new BigDecimal("340000"));

        for (Job job: result){
            System.out.println(result.getFirst().getSalary());
        }
        assertThat(result).isNotEmpty();

    }

    @Test
    void testSearchByTitle(){
        List<Job> result=jobRepo.searchByTitle("developer");

        System.out.println("Jobs found: " + result.size());
        result.forEach(j -> System.out.println(j.getTitle()));

        assertThat(result).hasSize(2);

    }

    private void createJob(String title, String location, String jobtype, BigDecimal salary) {
        Job job = new Job();
        job.setTitle(title);
        job.setLocation(location);
        job.setJobtype(jobtype);
        job.setSalary(salary);
    }

    @Test
    void testCountByLocation(){
        createJob("Backend Dev", "Pune", "Full-time", new BigDecimal("800000"));
        createJob("Frontend Dev", "Pune", "Remote", new BigDecimal("600000"));
        createJob("DevOps", "Bangalore", "Full-time", new BigDecimal("900000"));

        long count= jobRepo.countByLocation("Pune");
        System.out.println("Pune job count: "+count);

        assertThat(count).isEqualTo(2);

    }

    @Test
    void basicPagination(){

        for (int i=0; i<10; i++){
            createJob("Job" + i ,"Pune","Fulltime",new BigDecimal(i* 900000));
        }

        Pageable pageable= PageRequest.of(0,3);
        Page<Job> result = jobRepo.findAll(pageable);

        System.out.println("Total jobs in DB   : " + result.getTotalElements());
        System.out.println("Total pages        : " + result.getTotalPages());
        System.out.println("Jobs on this page  : " + result.getContent().size());
        System.out.println("Current page number: " + result.getNumber());
        System.out.println("Is first page      : " + result.isFirst());
        System.out.println("Is last page       : " + result.isLast());

        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);      // 10 jobs / 3 per page = 4 pages
        assertThat(result.getContent()).hasSize(3);           // page has 3 jobs
        assertThat(result.getNumber()).isEqualTo(0);
    }

    @Test
    void usingOpt(){
        Page<Job> result=jobRepo.findAll(PageRequest.of(0,3));

        result.getContent().forEach(job -> {
            System.out.println(job.getTitle()+"->"+job.getCompany());
        });

        assertThat(result.getContent()).hasSize(3);
    }






}

