package com.campusconnect.config;

import com.campusconnect.entity.*;
import com.campusconnect.repository.ApplicationRepository;
import com.campusconnect.repository.OpportunityRepository;
import com.campusconnect.repository.SkillRepository;
import com.campusconnect.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SkillRepository skillRepository;
    private final StudentRepository studentRepository;
    private final OpportunityRepository opportunityRepository;
    private final ApplicationRepository applicationRepository;

    public DataInitializer(SkillRepository skillRepository,
                           StudentRepository studentRepository,
                           OpportunityRepository opportunityRepository,
                           ApplicationRepository applicationRepository) {
        this.skillRepository = skillRepository;
        this.studentRepository = studentRepository;
        this.opportunityRepository = opportunityRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (skillRepository.count() > 0) {
            System.out.println("🌱 Database already initialized. Skipping seed data.");
            return;
        }

        System.out.println("🌱 Initializing CampusConnect platform with seed data...");

        // 1. Seed Skills
        Skill java = new Skill("Java", SkillCategory.PROGRAMMING_LANGUAGE, "Core Java, OOP, Collections, Multithreading, Streams API");
        Skill springBoot = new Skill("Spring Boot", SkillCategory.BACKEND, "Spring MVC, REST APIs, Spring Data JPA, Hibernate, Bean Validation");
        Skill react = new Skill("React", SkillCategory.FRONTEND, "Modern React, Functional Components, Hooks (useState, useEffect), Axios");
        Skill mysql = new Skill("MySQL", SkillCategory.DATABASE, "Relational Database Design, SQL Queries, JDBC, Indexing, Transactions");
        Skill docker = new Skill("Docker", SkillCategory.DEVOPS, "Containerization, Dockerfile, Container orchestration basics");
        Skill python = new Skill("Python", SkillCategory.PROGRAMMING_LANGUAGE, "Python scripting, data processing, backend scripting");
        Skill restApi = new Skill("REST APIs", SkillCategory.BACKEND, "RESTful API design, HTTP verbs, JSON data handling, Postman");
        Skill git = new Skill("Git & GitHub", SkillCategory.DEVOPS, "Version control, branching strategies, Git pull requests, CI/CD basics");
        Skill aws = new Skill("AWS Cloud", SkillCategory.CLOUD, "Amazon Web Services: EC2, S3, RDS, Lambda deployment basics");
        Skill ts = new Skill("TypeScript", SkillCategory.FRONTEND, "Type-safe JavaScript development, interfaces, generics");
        Skill ml = new Skill("Machine Learning", SkillCategory.AI_ML, "Scikit-Learn, data modeling, feature engineering, classification");
        Skill dsa = new Skill("Data Structures & Algorithms", SkillCategory.CORE_CS, "Arrays, Trees, Graphs, Sorting, Dynamic Programming");

        List<Skill> allSkills = Arrays.asList(java, springBoot, react, mysql, docker, python, restApi, git, aws, ts, ml, dsa);
        skillRepository.saveAll(allSkills);

        // 2. Seed Students
        Student s1 = new Student("Shruthi Sharma", "shruthi.sharma@campus.edu", "21CS042", "Computer Science & Engineering", 4, 9.2);
        s1.setPhone("+91 98765 43210");
        s1.setBio("Passionate Full-Stack Developer specializing in Spring Boot microservices and React web frontends.");
        s1.setGithubUrl("https://github.com/shruthi-sharma");
        s1.setLinkedinUrl("https://linkedin.com/in/shruthi-sharma");
        s1.setSkills(new HashSet<>(Arrays.asList(java, springBoot, react, mysql, restApi, git)));

        Student s2 = new Student("Aarav Patel", "aarav.patel@campus.edu", "21IT019", "Information Technology", 4, 8.7);
        s2.setPhone("+91 98111 22334");
        s2.setBio("Backend enthusiast passionate about scalable APIs, MySQL database optimization, and Docker containers.");
        s2.setGithubUrl("https://github.com/aarav-patel");
        s2.setLinkedinUrl("https://linkedin.com/in/aarav-patel");
        s2.setSkills(new HashSet<>(Arrays.asList(java, springBoot, mysql, docker, restApi, dsa)));

        Student s3 = new Student("Ananya Rao", "ananya.rao@campus.edu", "22CS088", "Computer Science & Engineering", 3, 9.5);
        s3.setPhone("+91 97222 33445");
        s3.setBio("Frontend architect and UI designer. Loves crafting accessible React applications and responsive web systems.");
        s3.setGithubUrl("https://github.com/ananya-rao");
        s3.setLinkedinUrl("https://linkedin.com/in/ananya-rao");
        s3.setSkills(new HashSet<>(Arrays.asList(react, ts, git, restApi)));

        Student s4 = new Student("Rohan Verma", "rohan.verma@campus.edu", "22AI015", "Artificial Intelligence & Data Science", 3, 8.4);
        s4.setPhone("+91 96333 44556");
        s4.setBio("AI/ML researcher working with predictive models, Python, and data pipeline architectures.");
        s4.setGithubUrl("https://github.com/rohan-verma");
        s4.setLinkedinUrl("https://linkedin.com/in/rohan-verma");
        s4.setSkills(new HashSet<>(Arrays.asList(python, ml, mysql, dsa)));

        Student s5 = new Student("Diya Menon", "diya.menon@campus.edu", "23CS101", "Computer Science & Engineering", 2, 7.8);
        s5.setPhone("+91 95444 55667");
        s5.setBio("Sophomore passionate about full stack foundations, Java programming, and open-source contributions.");
        s5.setGithubUrl("https://github.com/diya-menon");
        s5.setLinkedinUrl("https://linkedin.com/in/diya-menon");
        s5.setSkills(new HashSet<>(Arrays.asList(java, mysql, git)));

        studentRepository.saveAll(Arrays.asList(s1, s2, s3, s4, s5));

        // 3. Seed Opportunities
        Opportunity o1 = new Opportunity("Java Full-Stack Software Engineer Intern", "Google Labs", OpportunityType.INTERNSHIP, "Bengaluru (Hybrid)", WorkMode.HYBRID);
        o1.setStipendOrSalary("₹45,000 / month");
        o1.setMinCgpa(8.5);
        o1.setDeadline(LocalDate.now().plusMonths(2));
        o1.setDescription("Build and scale next-generation student collaborative systems using modern Java 21, Spring Boot REST microservices, React UI components, and MySQL. Great mentorship and stipend package.");
        o1.setRequiredSkills(new HashSet<>(Arrays.asList(java, springBoot, react, mysql, restApi)));

        Opportunity o2 = new Opportunity("Graduate Software Engineer - Campus Drive 2026", "TCS Digital", OpportunityType.CAMPUS_DRIVE, "Hyderabad (On-site)", WorkMode.ON_SITE);
        o2.setStipendOrSalary("₹9.0 LPA");
        o2.setMinCgpa(7.5);
        o2.setDeadline(LocalDate.now().plusMonths(3));
        o2.setDescription("Full-time campus placement for graduating students. Roles in enterprise microservices, database management, and high-performance Java systems.");
        o2.setRequiredSkills(new HashSet<>(Arrays.asList(java, springBoot, mysql, dsa)));

        Opportunity o3 = new Opportunity("Frontend UI/UX Engineering Intern", "Zomato Tech", OpportunityType.INTERNSHIP, "Gurugram (Hybrid)", WorkMode.HYBRID);
        o3.setStipendOrSalary("₹35,000 / month");
        o3.setMinCgpa(7.0);
        o3.setDeadline(LocalDate.now().plusMonths(1));
        o3.setDescription("Join our core consumer experience team to build dynamic React components, state workflows, responsive web apps, and integrate high-throughput REST APIs.");
        o3.setRequiredSkills(new HashSet<>(Arrays.asList(react, ts, restApi, git)));

        Opportunity o4 = new Opportunity("Cloud & DevOps Engineering Trainee", "Amazon Web Services (AWS)", OpportunityType.INTERNSHIP, "Bengaluru (Hybrid)", WorkMode.HYBRID);
        o4.setStipendOrSalary("₹50,000 / month");
        o4.setMinCgpa(8.0);
        o4.setDeadline(LocalDate.now().plusWeeks(6));
        o4.setDescription("Hands-on training in Docker containers, AWS Cloud infrastructure, CI/CD pipelines, and microservice monitoring.");
        o4.setRequiredSkills(new HashSet<>(Arrays.asList(docker, aws, git, python)));

        Opportunity o5 = new Opportunity("Smart Campus AI Hackathon 2026", "National Innovation Cell", OpportunityType.HACKATHON, "New Delhi / Hybrid", WorkMode.REMOTE);
        o5.setStipendOrSalary("₹3,00,000 Prize Pool");
        o5.setMinCgpa(0.0);
        o5.setDeadline(LocalDate.now().plusDays(25));
        o5.setDescription("Develop innovative AI-driven student platforms and automated campus management systems. Cash prizes, direct incubation support, and corporate sponsorship.");
        o5.setRequiredSkills(new HashSet<>(Arrays.asList(python, ml, react, restApi)));

        opportunityRepository.saveAll(Arrays.asList(o1, o2, o3, o4, o5));

        // 4. Seed Applications
        Application a1 = new Application(s1, o1, "https://campusconnect.edu/resumes/shruthi-resume.pdf", "I have hands-on experience building Spring Boot and React applications with full CRUD, validation, and REST API integration.", 100);
        a1.setStatus(ApplicationStatus.SHORTLISTED);
        a1.setAppliedDate(LocalDateTime.now().minusDays(5));

        Application a2 = new Application(s2, o2, "https://campusconnect.edu/resumes/aarav-resume.pdf", "Strong foundation in Java OOP, database indexing, and REST services.", 100);
        a2.setStatus(ApplicationStatus.UNDER_REVIEW);
        a2.setAppliedDate(LocalDateTime.now().minusDays(3));

        Application a3 = new Application(s3, o3, "https://campusconnect.edu/resumes/ananya-resume.pdf", "Passionate frontend developer experienced in modern React hooks and TypeScript design systems.", 100);
        a3.setStatus(ApplicationStatus.SELECTED);
        a3.setAppliedDate(LocalDateTime.now().minusDays(7));

        Application a4 = new Application(s4, o5, "https://campusconnect.edu/resumes/rohan-resume.pdf", "Excited to propose a machine learning recommendation engine for the hackathon challenge.", 75);
        a4.setStatus(ApplicationStatus.APPLIED);
        a4.setAppliedDate(LocalDateTime.now().minusDays(1));

        Application a5 = new Application(s1, o3, "https://campusconnect.edu/resumes/shruthi-resume.pdf", "Skilled in React and API consumption via Axios.", 75);
        a5.setStatus(ApplicationStatus.UNDER_REVIEW);
        a5.setAppliedDate(LocalDateTime.now().minusDays(2));

        applicationRepository.saveAll(Arrays.asList(a1, a2, a3, a4, a5));

        System.out.println("✅ CampusConnect demo data successfully loaded into database!");
    }
}
