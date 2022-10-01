package service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import service.storageService.StorageFileNotFoundException;
import service.storageService.StorageService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.BDDMockito.then;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
public class FileUploadTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;

    //@Mock
    @Autowired
    private StorageService storageService;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldListAllFiles() throws Exception {

        MockMultipartFile multipartFile = new MockMultipartFile("file", "first.txt",
                "text/plain", "Spring Framework".getBytes());
        mvc.perform(multipart("/").file(multipartFile));

        multipartFile = new MockMultipartFile("file", "second.txt",
                "text/plain", "Spring Framework".getBytes());
        mvc.perform(multipart("/").file(multipartFile));


        mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(model().attribute("files",
                        Matchers.contains("http://localhost/files/first.txt",
                               "http://localhost/files/second.txt")));
    }

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        this.mvc.perform(multipart("/").file(multipartFile))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/"));

        then(this.storageService).should().store(multipartFile);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should404WhenMissingFile() throws Exception {


        Assert.assertThrows(StorageFileNotFoundException.class, ()->
                this.storageService.loadAsResource("test.txt")
        );

        this.mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
    }

}
