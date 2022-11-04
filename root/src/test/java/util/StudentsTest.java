package util;

import org.junit.*;
import static org.assertj.core.api.Assertions.*;

import model.*;

import java.util.*;

import static util.Students.*;

public class StudentsTest {

    private final Student s1  = new Student(
        "2099991", "Bill Hedgerow", new SchoolYear(2020), new SchoolYear(2022), "GB");

    private final Student s2  = new Student(
        "2099992", "Clarice Meadow", new SchoolYear(2020), new SchoolYear(2022), "CN");

    private final Student s3  = new Student(
        "2099993", "Elle Treestump", new SchoolYear(2020), new SchoolYear(2022), "GB");

    private final Student s4  = new Student(
        "1999994", "Elle Treestump", new SchoolYear(2019), new SchoolYear(2019), "IN");

    private final Student s5  = new Student(
        "1999995", "Freddie Riverbank", new SchoolYear(2019), new SchoolYear(2019), "GB");

    private final Student s6  = new Student(
        "2099996", "Gertrude Oxbow-Lake", new SchoolYear(2020), new SchoolYear(2020), "CN");

    private final Student s7  = new Student(
        "2199997", "Harry Woddland", new SchoolYear(2021), new SchoolYear(2023), "DK");

    private final Student s8  = new Student(
        "2199998", "Ian Wildflower", new SchoolYear(2021), new SchoolYear(2023), "GB");

    private final Student s9  = new Student(
        "2299999", "Janis Pasture", new SchoolYear(2022), new SchoolYear(2023), "FR");

    private final Student s10  = new Student(
        "2399990", "Kim Farmyard", new SchoolYear(2023), new SchoolYear(2023), "IN");

    private final Students students = students(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10);

    @Test
    public void testStartingBetween() {

        assertThat(students.startingBetween(2020, 2020))
            .containsExactlyInAnyOrder(s1, s2, s3, s6);

        assertThat(students.startingBetween(2019, 2022))
            .containsExactlyInAnyOrder(s1, s2, s3, s4, s5, s6, s7, s8, s9);

        assertThat(students.startingBetween(2019, 2019))
            .containsExactlyInAnyOrder(s4, s5);

        assertThat(students.startingBetween(2022, 2023))
            .containsExactlyInAnyOrder(s9, s10);

    }

    @Test
    public void testFinishingBetween() {

        assertThat(students.finishingBetween(2020, 2020))
            .containsExactlyInAnyOrder(s4, s5);

        assertThat(students.finishingBetween(2021, 2022))
            .containsExactlyInAnyOrder(s6);

        assertThat(students.finishingBetween(2021, 2024))
            .containsExactlyInAnyOrder(s1, s2, s3, s6, s7, s8, s9, s10);

        assertThat(students.finishingBetween(2024, 2025))
            .containsExactlyInAnyOrder(s7, s8, s9, s10);

    }

    @Test
    public void testGroupedByStartYear() {

        final Map<SchoolYear, ? extends Collection<Student>> grouped 
            = students.groupedByStartYear();

        final SchoolYear sy2019 = new SchoolYear(2019);
        final SchoolYear sy2020 = new SchoolYear(2020);
        final SchoolYear sy2021 = new SchoolYear(2021);
        final SchoolYear sy2022 = new SchoolYear(2022);
        final SchoolYear sy2023 = new SchoolYear(2023);

        assertThat(grouped)
            .containsOnlyKeys(sy2019, sy2020, sy2021, sy2022, sy2023);

        assertThat(grouped.get(sy2019)).containsExactlyInAnyOrder(s4, s5);
        assertThat(grouped.get(sy2020)).containsExactlyInAnyOrder(s1, s2, s3, s6);
        assertThat(grouped.get(sy2021)).containsExactlyInAnyOrder(s7, s8);
        assertThat(grouped.get(sy2022)).containsExactlyInAnyOrder(s9);
        assertThat(grouped.get(sy2023)).containsExactlyInAnyOrder(s10);

    }

    @Test
    public void testCountOfCourseLengthforStartYear() {

        final Map<Integer, Long> countsFor2019
            = students.countOfCourseLengthforStartYear(new SchoolYear(2019));

        final Map<Integer, Long> countsFor2020
            = students.countOfCourseLengthforStartYear(new SchoolYear(2020));

        final Map<Integer, Long> countsFor2021
            = students.countOfCourseLengthforStartYear(new SchoolYear(2021));

        final Map<Integer, Long> countsFor2022
            = students.countOfCourseLengthforStartYear(new SchoolYear(2022));

        final Map<Integer, Long> countsFor2023
            = students.countOfCourseLengthforStartYear(new SchoolYear(2023));

        assertThat(countsFor2019).containsOnlyKeys(1);
        assertThat(countsFor2019.get(1)).isEqualTo(2);

        assertThat(countsFor2020).containsOnlyKeys(1, 3);
        assertThat(countsFor2020.get(1)).isEqualTo(1);
        assertThat(countsFor2020.get(3)).isEqualTo(3);

        assertThat(countsFor2021).containsOnlyKeys(3);
        assertThat(countsFor2021.get(3)).isEqualTo(2);

        assertThat(countsFor2022).containsOnlyKeys(2);
        assertThat(countsFor2022.get(2)).isEqualTo(1);

        assertThat(countsFor2023).containsOnlyKeys(1);
        assertThat(countsFor2023.get(1)).isEqualTo(1);


    }

    @Test
    public void testCountOfCountryCodeForStartYear() {

        final Map<String, Long> countsFor2019
            = students.countOfCountryCodeForStartYear(new SchoolYear(2019));

        final Map<String, Long> countsFor2020
            = students.countOfCountryCodeForStartYear(new SchoolYear(2020));

        final Map<String, Long> countsFor2021
            = students.countOfCountryCodeForStartYear(new SchoolYear(2021));

        final Map<String, Long> countsFor2022
            = students.countOfCountryCodeForStartYear(new SchoolYear(2022));

        final Map<String, Long> countsFor2023
            = students.countOfCountryCodeForStartYear(new SchoolYear(2023));

        assertThat(countsFor2019).containsOnlyKeys("IN", "GB");
        assertThat(countsFor2019.get("IN")).isEqualTo(1);
        assertThat(countsFor2019.get("GB")).isEqualTo(1);

        assertThat(countsFor2020).containsOnlyKeys("GB", "CN");
        assertThat(countsFor2020.get("GB")).isEqualTo(2);
        assertThat(countsFor2020.get("CN")).isEqualTo(2);

        assertThat(countsFor2021).containsOnlyKeys("DK", "GB");
        assertThat(countsFor2021.get("DK")).isEqualTo(1);
        assertThat(countsFor2021.get("GB")).isEqualTo(1);

        assertThat(countsFor2022).containsOnlyKeys("FR");
        assertThat(countsFor2022.get("FR")).isEqualTo(1);

        assertThat(countsFor2023).containsOnlyKeys("IN");
        assertThat(countsFor2023.get("IN")).isEqualTo(1);

    }

    @Test
    public void testOrderedById() {

        assertThat(students(s3, s5, s2, s1, s6).orderedById())
            .containsExactly(s5, s1, s2, s3, s6);

        assertThat(students(s10, s9, s8, s7, s6).orderedById())
            .containsExactly(s6, s7, s8, s9, s10);

        assertThat(students(s4, s3, s5, s2, s1, s6, s10, s9, s8, s7).orderedById())
            .containsExactly(s4, s5, s1, s2, s3, s6, s7, s8, s9, s10);

    }

    @Test
    public void testOrderedByNameThenId() {

        assertThat(students(s3, s5, s2, s1, s6).orderedByNameThenId())
            .containsExactly(s1, s2, s3, s5, s6);

        assertThat(students(s10, s9, s8, s7, s6).orderedByNameThenId())
            .containsExactly(s6, s7, s8, s9, s10);

        assertThat(students(s3, s4, s5, s2, s1, s6, s10, s9, s8, s7).orderedByNameThenId())
            .containsExactly(s1, s2, s4, s3, s5, s6, s7, s8, s9, s10);

    }

}
