package com.example.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.SelectOption;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
    }
}
