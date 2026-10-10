package com.aiskill.platform.service;

import com.aiskill.platform.model.*;
import com.aiskill.platform.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataInitializerService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final CodingProblemRepository problemRepository;
    private final RagService ragService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializerService(UserRepository userRepository,
                                  TestRepository testRepository,
                                  QuestionRepository questionRepository,
                                  CodingProblemRepository problemRepository,
                                  RagService ragService,
                                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.problemRepository = problemRepository;
        this.ragService = ragService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedUsers();
        seedAssessments();
        seedCodingProblems();
        seedStudyMaterials();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            User admin = new User("System Admin", "admin@skillai.com", passwordEncoder.encode("admin123"), Role.ROLE_ADMIN);
            admin.setTargetRole("Lead Platform Architect");
            admin.setBio("Platform administrator overseeing technical curriculums.");
            userRepository.save(admin);

            User student = new User("Alex Johnson", "student@skillai.com", passwordEncoder.encode("student123"), Role.ROLE_STUDENT);
            student.setTargetRole("Full Stack Software Engineer");
            student.setBio("Passionate developer eager to master Spring Boot and React.");
            student.setSkills("Java, Spring Boot, React, MySQL, Docker");
            userRepository.save(student);

            User student2 = new User("Priya Sharma", "priya@skillai.com", passwordEncoder.encode("student123"), Role.ROLE_STUDENT);
            student2.setTargetRole("Backend Java Engineer");
            student2.setBio("Specializing in high-throughput microservices and distributed systems.");
            userRepository.save(student2);
        }
    }

    private void seedAssessments() {
        if (testRepository.count() == 0) {
            // Test 1: Java Core & OOP
            Test t1 = new Test("Java Core & OOP Fundamentals", "Java", "Intermediate", 15,
                    "Mastery of Java memory management, collections framework, inheritance, polymorphism, and Java 21 features.");
            t1 = testRepository.save(t1);

            questionRepository.save(new Question(t1,
                    "Which collection class in Java allows null elements, maintains insertion order, and is non-synchronized?",
                    "ArrayList", "HashSet", "TreeSet", "Hashtable",
                    "A", "Collections",
                    "ArrayList permits null elements, maintains insertion order based on indexing, and is not thread-safe (non-synchronized)."));

            questionRepository.save(new Question(t1,
                    "In Java memory management, where are local primitive variables and object references stored?",
                    "Heap Memory", "Stack Memory", "Metaspace", "Garbage Collector Pool",
                    "B", "Memory",
                    "Local variables (both primitives and object reference pointers) are allocated on the Stack frames of the executing thread."));

            questionRepository.save(new Question(t1,
                    "What happens when you declare a record class in Java 14+?",
                    "It cannot implement any interfaces",
                    "It automatically generates final fields, canonical constructor, getters, equals, and hashCode",
                    "It allows mutating its state via setter methods",
                    "It cannot be used as a DTO",
                    "B", "Modern Java",
                    "Records are immutable data carriers that provide concise syntax by generating private final fields, canonical constructor, and accessors."));

            questionRepository.save(new Question(t1,
                    "Which functional interface in java.util.function accepts an argument of type T and returns a boolean?",
                    "Consumer<T>", "Function<T, R>", "Predicate<T>", "Supplier<T>",
                    "C", "Generics & Streams",
                    "Predicate<T> defines the abstract method 'boolean test(T t)', commonly used in filter operations."));

            // Test 2: Spring Boot & Microservices
            Test t2 = new Test("Spring Boot & Microservices Architecture", "Spring Boot", "Advanced", 20,
                    "Evaluates dependency injection, bean scopes, Spring Security, JPA transactions, and microservices patterns.");
            t2 = testRepository.save(t2);

            questionRepository.save(new Question(t2,
                    "What is the default scope of a Spring bean managed by the ApplicationContext?",
                    "Prototype", "Singleton", "Request", "Session",
                    "B", "Core Spring",
                    "The default bean scope in Spring is Singleton, meaning one instance per Spring IoC container."));

            questionRepository.save(new Question(t2,
                    "Which annotation combines @Controller and @ResponseBody?",
                    "@Service", "@Component", "@RestController", "@Endpoint",
                    "C", "Spring MVC",
                    "@RestController is a convenience stereotype annotation combining @Controller and @ResponseBody."));

            questionRepository.save(new Question(t2,
                    "In Spring Data JPA, what does the @Transactional attribute propagation = Propagation.REQUIRES_NEW do?",
                    "Joins the current transaction if one exists",
                    "Suspends the current transaction and creates a new independent transaction",
                    "Throws an exception if an active transaction is present",
                    "Executes outside the transaction scope",
                    "B", "Transactions",
                    "Propagation.REQUIRES_NEW always suspends the current transaction and starts a completely new physical transaction."));

            questionRepository.save(new Question(t2,
                    "How does Spring Security filter incoming requests before they reach the DispatcherServlet?",
                    "Via Servlet Filter Chain (DelegatingFilterProxy & SecurityFilterChain)",
                    "Via Spring AOP Around advice only",
                    "Via database triggers",
                    "Via client-side validation cookies",
                    "A", "Security",
                    "Spring Security hooks into standard Servlet filters via DelegatingFilterProxy which delegates to the Spring SecurityFilterChain."));

            // Test 3: React.js & Frontend Architecture
            Test t3 = new Test("React.js & Frontend Architecture", "React", "Intermediate", 15,
                    "Covers Virtual DOM, hooks, state management, reconciliation, and component lifecycles.");
            t3 = testRepository.save(t3);

            questionRepository.save(new Question(t3,
                    "What is the primary purpose of the React useMemo hook?",
                    "To trigger side effects after render",
                    "To memoize expensive calculation results between re-renders",
                    "To create two-way data bindings",
                    "To replace the Redux store",
                    "B", "Hooks",
                    "useMemo caches the result of an expensive calculation and only re-computes when dependencies change."));

            questionRepository.save(new Question(t3,
                    "Why must keys be unique among siblings in React lists?",
                    "Keys are used for CSS selector matching",
                    "Keys help React identify which items have changed, been added, or removed during reconciliation",
                    "Keys are mandatory HTML5 attributes",
                    "Keys encrypt state data",
                    "B", "Reconciliation",
                    "React uses the key attribute during reconciliation to match children across renders efficiently without re-mounting identical DOM nodes."));
        }
    }

    private void seedCodingProblems() {
        if (problemRepository.count() == 0) {
            CodingProblem p1 = new CodingProblem();
            p1.setTitle("Two Sum Target Index");
            p1.setCategory("Arrays & HashMaps");
            p1.setDifficulty("Easy");
            p1.setDescription("Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.");
            p1.setSampleInput("[2, 7, 11, 15], target = 9");
            p1.setSampleOutput("[0, 1]");
            p1.setHints("Use a HashMap to store the complement value (target - num) and its index for O(n) lookup.");
            p1.setStarterCodeJava("""
public class Solution {
    public int[] twoSum(int[] nums, int target) {
        // Your O(n) solution here
        return new int[]{0, 1};
    }
}""");
            p1.setStarterCodePython("""
def two_sum(nums, target):
    # Your solution here
    return [0, 1]
""");
            p1.setStarterCodeJs("""
function twoSum(nums, target) {
    // Your solution here
    return [0, 1];
}
""");
            p1.setTestCasesJson("""
[
  {"input": "nums = [2,7,11,15], target = 9", "expected": "[0, 1]"},
  {"input": "nums = [3,2,4], target = 6", "expected": "[1, 2]"},
  {"input": "nums = [3,3], target = 6", "expected": "[0, 1]"}
]""");
            problemRepository.save(p1);

            CodingProblem p2 = new CodingProblem();
            p2.setTitle("Valid Palindrome String");
            p2.setCategory("Two Pointers");
            p2.setDifficulty("Easy");
            p2.setDescription("A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward.\n\nReturn true if it is a palindrome, or false otherwise.");
            p2.setSampleInput("\"A man, a plan, a canal: Panama\"");
            p2.setSampleOutput("true");
            p2.setHints("Use two pointers starting at both ends moving inwards, skipping non-alphanumeric characters.");
            p2.setStarterCodeJava("""
public class Solution {
    public boolean isPalindrome(String s) {
        // Implement two-pointer check
        return true;
    }
}""");
            p2.setStarterCodePython("""
def is_palindrome(s: str) -> bool:
    # Implement two-pointer check
    return True
""");
            p2.setStarterCodeJs("""
function isPalindrome(s) {
    // Implement two-pointer check
    return true;
}
""");
            p2.setTestCasesJson("""
[
  {"input": "s = 'A man, a plan, a canal: Panama'", "expected": "true"},
  {"input": "s = 'race a car'", "expected": "false"},
  {"input": "s = ' '", "expected": "true"}
]""");
            problemRepository.save(p2);

            CodingProblem p3 = new CodingProblem();
            p3.setTitle("Longest Substring Without Repeating Characters");
            p3.setCategory("Sliding Window");
            p3.setDifficulty("Medium");
            p3.setDescription("Given a string `s`, find the length of the longest substring without duplicate characters.");
            p3.setSampleInput("\"abcabcbb\"");
            p3.setSampleOutput("3");
            p3.setHints("Maintain a sliding window [left, right] and track seen characters with their latest indices.");
            p3.setStarterCodeJava("""
public class Solution {
    public int lengthOfLongestSubstring(String s) {
        // Sliding window implementation
        return 3;
    }
}""");
            p3.setStarterCodePython("""
def length_of_longest_substring(s: str) -> int:
    return 3
""");
            p3.setStarterCodeJs("""
function lengthOfLongestSubstring(s) {
    return 3;
}
""");
            p3.setTestCasesJson("""
[
  {"input": "s = 'abcabcbb'", "expected": "3"},
  {"input": "s = 'bbbbb'", "expected": "1"},
  {"input": "s = 'pwwkew'", "expected": "3"}
]""");
            problemRepository.save(p3);
        }
    }

    private void seedStudyMaterials() {
        if (ragService.getAllDocuments().isEmpty()) {
            ragService.uploadDocument(
                    "Spring Boot 3 & Security Architecture Guide",
                    "Spring Boot",
                    "Spring Boot 3 brings streamlined configuration, native image compilation support with GraalVM, and upgraded baseline Java 17/21 requirements. " +
                            "Spring Security 6 introduces functional security configuration replacing WebSecurityConfigurerAdapter with SecurityFilterChain beans. " +
                            "Authentication is typically managed through stateless JWT bearer tokens validated in custom OncePerRequestFilter implementations. " +
                            "When configuring CORS, explicit allowed origins or allowedOriginPatterns must be configured along with permitted HTTP methods (GET, POST, PUT, DELETE, OPTIONS). " +
                            "Spring Data JPA abstracts boilerplate queries, offering dynamic repositories, pagination, and projection support with Hibernate ORM."
            );

            ragService.uploadDocument(
                    "React 18 & State Management Deep Dive",
                    "React",
                    "React 18 introduces concurrent rendering features such as automatic batching, startTransition, and Suspense for data fetching. " +
                            "Components manage internal state via useState and side effects via useEffect. " +
                            "To prevent unnecessary re-computations of complex derived data, useMemo can be used. " +
                            "To maintain stable function references passed down to memoized child components, useCallback is essential. " +
                            "Cleanups in useEffect prevent memory leaks, especially when dealing with WebSocket connections, interval timers, or external subscriptions."
            );

            ragService.uploadDocument(
                    "Database Indexing & Query Optimization Handbook",
                    "Database & SQL",
                    "Relational databases rely on B-Tree and B+ Tree index structures to provide logarithmic lookup time O(log N). " +
                            "Composite indexes should follow the leftmost prefix rule. " +
                            "Avoid SELECT * queries in high-volume production systems to minimize I/O overhead. " +
                            "Understand ACID guarantees: Atomicity (all or nothing), Consistency (valid state transitions), Isolation (transaction concurrency control), and Durability (WAL write-ahead logging). " +
                            "Connection pooling using HikariCP significantly improves throughput by reusing expensive physical database sockets."
            );
        }
    }
}
