package util;

import java.util.*;

import model.*;


import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;
import static java.util.Collections.*;
import static java.util.Arrays.*;

// I was originally going to use an SQLite ORM 
// and create a dependency-injected repository class to persist students,
// as task expectations were not 100% clear, but the method signatures look like
// they expect application logic and I personally think that coding
// a sorting algorithm like quicksort/merge sort is more impressive 
// than sorting by passing over an SQL string. So no data persistence.

public class Students {

    // Holds the collection of student for the methods to operate on
    private final Collection<Student> students;

    // Private constructor, used by the factory method
    private Students(Student... students) {
        
        this.students = unmodifiableCollection(asList(students));
        
    }

    // Public factory method
    public static Students students(Student... students) {
        
        return new Students(students);

    }
    
    // Just an abstraction for the first two methods, which use mostly the same logic.
    // I am aware of the disadvantages of primitive obsession and passing year
    // as an integer, but I don't think it's a problem in this task.
    private boolean yearBetween(int from, int to, int year) {
    	if (year >= from && year <= to) {
    		return true;
    	}
    	return false;
    }

     // Return all students that start(ed) between the the two
     // given years (inclusive of from and to)
    public Collection<Student> startingBetween(int from, int to) {
    	ArrayList<Student> result = new ArrayList<Student>();
    	for (Student student : students) {
    		if (yearBetween(from, to, student.getStartYear().getStart()))
    		{
    			result.add(student);
    		}
    	}
        return result;
    }

     // Return all students that finish(ed) between the the two given
     // years (inclusive of from and to).
    public Collection<Student> finishingBetween(int from, int to) {
    	ArrayList<Student> result = new ArrayList<Student>();
    	for (Student student : students)
    	{
    		if (yearBetween(from, to, student.getEndYear().getEnd())) {
    			result.add(student);
    		}
    	}
        return result;
    }

     // Return all students grouped by start year.
     // The result should be a Map from school year to a collection of students.
    public Map<SchoolYear, ? extends Collection<Student>> groupedByStartYear() {
    	// I appreciate the generated equals and hash code override provided here for the school year.
    	// I have had a growing appreciation of immutability after learning functional programming,
    	// and I know by default objects would be compared by reference rather than value,
    	// which would make this problem a little more difficult since I would need to either override
    	// the default SchoolYear equals/compare or use a primitive.
    	
    	Map<SchoolYear, ArrayList<Student>> result = new HashMap<SchoolYear, ArrayList<Student>>();
    	
    	for (Student student : students)
    	{
    		SchoolYear year = student.getStartYear();
    		if (!result.containsKey(year)) {
    			result.put(year, new ArrayList<Student>());
    		}
    		result.get(year).add(student);
    	}
    	
        return result;

    }
    
    private Integer getCourseLength(Student student) {
    	return student.getEndYear().getEnd() - student.getStartYear().getStart();
    }

     // For the given school year, return counts of the number of students for
     // each course length (for students starting that year).
     // The result should be a Map from course length to a count of students.
    public Map<Integer, Long> countOfCourseLengthforStartYear(SchoolYear startYear) {
    	// Using a hash map because it helps with speed in this scenario compared to other types (like TreeMap).
    	// HashMaps can access keys very quickly in O(1) time, while TreeMaps use BSTs which are
    	// better for other scenarios (like VS Code's text buffer which uses a red-black tree).
    	// Although the O(n) for loop really makes this quite pointless.
    	
    	HashMap<Integer, Long> result = new HashMap<Integer, Long>();

    	for (Student student : students) {    		
    		if (student.getStartYear().equals(startYear)) {
    			Integer courseLength = getCourseLength(student);
    			if (!result.containsKey(courseLength)) {
    				result.put(courseLength, 1L);
    			} else {
    				result.replace(courseLength, result.get(courseLength) + 1L);
    			}
    		}
    	}

    	return result;
    }

     // For the given school year, return counts of the number of students for
     // each country code (for students starting that year).
     // The result should be a Map from country code to a count of students.
    public Map<String, Long> countOfCountryCodeForStartYear(SchoolYear startYear) {
    	// I mentioned primitive obsession before and I think country code would make a good data type
    	// (like an enum) for a type-safe system. Just giving my thoughts on reading these tasks.
        // The for loop and if-statements in this task and in the task directly above are very similar
    	// (similarity/repetition is a good sign that it's time to abstract), but it's not worth it
    	// in an imperative/OOP language as it's more complicated there than in a functional language.
    	
    	HashMap<String, Long> result = new HashMap<String, Long>();

    	for (Student student : students) {
    		if (student.getStartYear().equals(startYear)) {
    			String code = student.getCountryCode();
    			if (!result.containsKey(code)) {
    				result.put(code, 1L);
    			} else {
    				result.replace(code, result.get(code) + 1L);
    			}
    		}
    	}
    	
    	return result;
    }
    
    // We have two tasks that require sorting by different fields
    // (first by just the ID and secondly by the name and the ID).
    // We can abstract the core algorithm and select different comparators
    // by using enums and choosing the correct one with an if/switch statement.
    // Similarly to command/strategy pattern (or discriminated unions in FP).
    enum SortField {ID, NameAndThenID}
    
    private List<Student> sortByID(Student stu1, Student stu2) {
    	ArrayList<Student> list = new ArrayList<Student>();
    	String id1 = stu1.getId();
    	String id2 = stu2.getId();
    	
    	if (id1.compareTo(id2) < 0) {
    		list.add(stu1);
    		list.add(stu2);
    	} else {
    		list.add(stu2);
    		list.add(stu1);
    	}
    	
    	return list;
    }
    
    private List<Student> sortByNameAndThenID(Student stu1, Student stu2) {
    	String name1 = stu1.getName();
    	String name2 = stu2.getName();
    	
    	if (name1 == name2) {
    		return sortByID(stu1, stu2);
    	}
    	
    	ArrayList<Student> list = new ArrayList<Student>();
    	if (name1.compareTo(name2) < 0) {
    		list.add(stu1);
    		list.add(stu2);
    	} else {
    		list.add(stu2);
    		list.add(stu1);
    	}
    	return list;
    }
    
    private List<Student> sortByHandler(Student stu1, Student stu2, SortField field) {
    	switch (field) {
    	case ID: 
    		return sortByID(stu1, stu2);
		case NameAndThenID:
			return sortByNameAndThenID(stu1, stu2);
			
		// The default case never actually gets called, but it's required by the compiler.
		default:
			return new ArrayList<Student>();
		}
    }
    
    // Sometimes we want to return a list after comoparison and other times we just want a bool.
    private boolean isBefore(Student stu1, Student stu2, SortField field) {
    	List<Student> list = sortByHandler(stu1, stu2, field);
    	
    	if (list.get(0) == stu1) {
    		return true;
    	}
    	return false;
    }
    
    private List<Student> mergeSort(List<Student> list, SortField field) {
    	if (list.size() <= 1) {
    		return list;
    	} else if (list.size() == 2) {
    		return sortByHandler(list.get(0), list.get(1), field);
    	}
    	
    	// partition
    	Integer middle = list.size() / 2;
    	List<Student> left = list.subList(0, middle);
    	List<Student> right = list.subList(middle, list.size());
    	System.out.println("at partition");
    	System.out.println(left);
    	System.out.println(right);
    	System.out.println("end partition");
    	
    	// recursion, split until we have either two or one
    	left = mergeSort(left, field);
    	right = mergeSort(right, field);

    	// copy using temporary arrays and join to final array
    	// stacks are also a good choice for this part because of their fifo/popping functionality
    	Integer i = 0, j = 0;
    	List<Student> tempLeft = new ArrayList<Student>();
    	List<Student> tempRight = new ArrayList<Student>();
    	    	
    	List<Student> result = new ArrayList<Student>();
    	
    	while (i < left.size() && j < right.size()) {
    		Student loopStu1 = left.get(i);
    		Student loopStu2 = right.get(j);
    		if (isBefore(loopStu1, loopStu2, field)) {
    			result.add(loopStu1);
    			i += 1;
    		} else {
    			result.add(loopStu2);
    			j += 1;
    		}
    	}
    	
    	// if arrays were not exhausted, need to add to result
    	if (i <= left.size()) {
    		System.out.println("last left if");
    		System.out.println(left.subList(i, left.size()));
    		result.addAll(left.subList(i, left.size()));
    	}
    	
    	if (j <= right.size()) {
    		System.out.println("last right if");
    		System.out.println(right);
    		result.addAll(right.subList(j, right.size()));
    	}
    	
    	System.out.println("result");
		System.out.println(result);

    	return result;
    }
    
    // Return a list of students sorted in ascending order by id
    public List<Student> orderedById() {
    	// Merge sort is a classically fast sorting algorithm, so I will choose that.
    	// I have the process memorised in my head as it struck me as a good algorithm for multi-threading,
    	// although I've learned now that data structures are often more important than CPU power.
    	// A multi-threaded bubble sort will still eventually be beaten by quicksort.
    	
    	// copying a list into another format can be expensive, but it's enough for the task.
    	List<Student> list = new ArrayList<Student>();
    	list.addAll(students);
    	
    	return mergeSort(list, SortField.ID);
    }    

    // Return a list of students sorted in ascending order by name,
    // if two students have the same name, they should be sorted by id
    public List<Student> orderedByNameThenId() {
    	List<Student> list = new ArrayList<Student>();
    	list.addAll(students);
    	
    	return mergeSort(list, SortField.NameAndThenID);
    }

}