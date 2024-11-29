package com.example.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightFormsTest {

    @Nested
    @DisplayName("Interacting with text fields")
    class WhenInteractingWithTextFields {

        @DisplayName("Complete the form")
        @Test
        void completeForm(Page page) throws URISyntaxException {

            page.navigate("https://practicesoftwaretesting.com/contact");

            var firstName = page.getByLabel("First name");
            var lastName = page.getByLabel("Last name");
            var email = page.getByLabel("Email address");
            var message = page.getByLabel("Message");
            var subject = page.getByLabel("Subject");
            var upload = page.getByLabel("Attachment");


            firstName.fill("Sarah-Jane");
            lastName.fill("Smith");
            email.fill("sarah-smith@example.com");
            message.fill("Hello World!");
            subject.selectOption(new SelectOption().setIndex(2));

            Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
            page.setInputFiles("#attachment", fileToUpload);

            assertThat(firstName).hasValue("Sarah-Jane");
            assertThat(lastName).hasValue("Smith");
            assertThat(email).hasValue("sarah-smith@example.com");
            assertThat(message).hasValue("Hello World!");
            assertThat(subject).hasValue("webmaster");


            String uploadedFile = upload.inputValue();
            Assertions.assertThat(uploadedFile).endsWith("sample-data.txt");

        }


        @DisplayName("Mandatory fields")
        @ParameterizedTest
        @ValueSource(strings = {"First name", "Last name", "Email address", "Message"})
        void mandatoryFields(String fieldName, Page page) {
            page.navigate("https://practicesoftwaretesting.com/contact");

            var firstName = page.getByLabel("First name");
            var lastName = page.getByLabel("Last name");
            var email = page.getByLabel("Email");
            var message = page.getByLabel("Message");
            var subject = page.getByLabel("Subject");
            var sendButton = page.getByText("Send");


            // fill in the field values

            firstName.fill("Sarah-Jane");
            lastName.fill("Smith");
            email.fill("sarah-smith@example.com");
            message.fill("Lorem ipsum dolor sit amet");
            subject.selectOption(new SelectOption().setIndex(2));


            page.getByLabel(fieldName).clear();

            sendButton.click();


            var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");

            assertThat(errorMessage).isVisible();


        }

    }
}
