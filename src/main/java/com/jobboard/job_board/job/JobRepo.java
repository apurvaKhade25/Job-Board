package com.jobboard.job_board.job;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.query.SelectionQuery;
import org.hibernate.query.sqm.internal.KeyBasedPagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface JobRepo extends JpaRepository<Job, Long> {
    List<Job> findBySalaryBetween(BigDecimal min, BigDecimal max);

    List<Job> findByJobtype(String jobtype);

    List<Job> findByLocationAndJobtype(String location, String jobtype);

//    List<Job> findByTitleContainingIgnoreCase(String keyword);

    List<Job> findByCompanyId(Long companyId);

    long countByLocation(String location);

    @Query("SELECT j FROM Job j WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Job> searchByTitle(@Param("keyword") String keyword);

    @Query("SELECT j FROM Job j WHERE j.salary >= :minSalary ORDER BY j.salary DESC")
    List<Job> findJobsAboveSalary(@Param("minSalary") BigDecimal minSalary);

    @Query("SELECT j FROM Job j WHERE j.location = :location AND j.salary >= :minSalary")
    List<Job> filterJobs(@Param("location") String location, @Param("minSalary") BigDecimal minSalary);

    @Query("SELECT j FROM Job j JOIN j.company c WHERE c.name = :name")
    List<Job> findByCompanyName(@Param("name") String name);


    //Pagination

    // pagination + sorting of all jobs

    // pagination + filter by location
    Page<Job> findByLocation(String  Location,Pageable pageable);

    // pagination + filter by jobtype
    Page<Job> findByJobtype(String Jobtype,Pageable pageable);

    // pagination + filter by keyword
    Page<Job> findByTitleContainingIgnoreCase(String Title,Pageable pageable);

    // pagination + filter by salary
    Page<Job> findBySalaryGreaterThan(BigDecimal Salary,Pageable pageable);

//    offset pagination
    @EntityGraph(attributePaths = {"company"})
    Page<Job> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"company"})   // while fetching jobs also load the company data
    @Query("SELECT j FROM Job j WHERE j.id> :cursor ORDER BY j.id ASC")
    List<Job> findJobsByCursor(@Param("cursor") Long Cursor, Pageable pageable); //no offset

    // first page — no cursor yet
    @EntityGraph(attributePaths = {"company"})
    @Query("SELECT j FROM Job j ORDER BY j.id ASC")
    List<Job> findFirstPage(Pageable pageable);

    // check if more exist
    boolean existsByIdGreaterThan(Long id);



}
