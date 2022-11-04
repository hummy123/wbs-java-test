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

     // For the given school year, return counts of the number of students for
     // each course length (for students starting that year).
     // The result should be a Map from course length to a count of students.
    public Map<Integer, Long> countOfCourseLengthforStartYear(SchoolYear startYear) {

        return emptyMap(); // todo: implement

    }

     // For the given school year, return counts of the number of students for
     // each country code (for students starting that year).
     // The result should be a Map from country code to a count of students.
    public Map<String, Long> countOfCountryCodeForStartYear(SchoolYear startYear) {

        return emptyMap(); // todo: implement

    }

    // Return a list of students sorted in ascending order by id
    public List<Student> orderedById() {

        return emptyList(); // todo: implement

    }    

    // Return a list of students sorted in ascending order by name,
    // if two students have the same name, they should be sorted by id
    public List<Student> orderedByNameThenId() {

        return emptyList(); // todo: implement

    }

}