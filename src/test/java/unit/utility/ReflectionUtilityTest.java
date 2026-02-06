package unit.utility;

import com.google.gson.annotations.SerializedName;
import org.junit.jupiter.api.Test;

import static io.github.eggy03.ferrumx.windows.utility.ReflectionUtility.getFromSerializedNames;
import static org.assertj.core.api.Assertions.assertThat;

class ReflectionUtilityTest {

    @Test
    void getFromSerializedNames_withAnnotatedFields_success() {

        String expectedString = "field_one, field_two, field_three";
        String actualString = getFromSerializedNames(MockWithAnnotatedFields.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    void getFromSerializedNames_withoutAnnotatedFields_success() {

        String expectedString = "fieldOne, fieldTwo, fieldThree";
        String actualString = getFromSerializedNames(MockWithoutAnnotatedFields.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    void getFromSerializedNames_withAbstractClass_success_emptyString() {

        String actualString = getFromSerializedNames(MockAbstractClass.class);
        assertThat(actualString).isNotNull().isEmpty();
    }

    @Test
    void getFromSerializedNames_withAnnotatedFields_inheritedFromAnotherClass_success() {

        String expectedString = "field_four"; // inherited fields are not included
        String actualString = getFromSerializedNames(ExtensionOfMockWithAnnotatedFields.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @SuppressWarnings("unused")
    static class MockWithAnnotatedFields { // inner test class where fields are annotated with gson @SerializedName

        @SerializedName("field_one")
        String fieldOne;

        @SerializedName("field_two")
        String fieldTwo;

        @SerializedName("field_three")
        String fieldThree;
    }

    @SuppressWarnings("unused")
    static class MockWithoutAnnotatedFields {

        String fieldOne;
        String fieldTwo;
        String fieldThree;
    }

    static class MockAbstractClass {

    }

    @SuppressWarnings("unused")
    static class ExtensionOfMockWithAnnotatedFields extends MockWithAnnotatedFields {

        @SerializedName("field_four")
        String fieldFour;
    }
}
