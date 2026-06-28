package com.example.kametud_catalog.controller;

import com.example.kametud_catalog.client.IdentityClient;
import com.example.kametud_catalog.client.StudentStatusResponse;
import com.example.kametud_catalog.dto.GigCreateRequest;
import com.example.kametud_catalog.dto.GigTierDto;
import com.example.kametud_catalog.entity.Gig;
import com.example.kametud_catalog.repository.GigRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GigControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GigRepository gigRepository;

    @MockitoBean
    private IdentityClient identityClient;

    @BeforeEach
    void setUp() {
        gigRepository.deleteAll();
        reset(identityClient);
    }

    @Test
    void createPublishedGigShouldPersistJsonTiersAndReturnCreated() throws Exception {
        UUID studentId = UUID.randomUUID();
        GigCreateRequest request = createRequest(studentId, true);
        when(identityClient.getStudentStatus(studentId)).thenReturn(new StudentStatusResponse(true, false));

        mockMvc.perform(post("/api/gigs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/gigs/")))
                .andExpect(jsonPath("$.published").value(true))
                .andExpect(jsonPath("$.tierBasique.title").value("Basic"))
                .andExpect(jsonPath("$.tierPremium.deliveryDays").value(2));

        List<Gig> persistedGigs = gigRepository.findAll();
        assertThat(persistedGigs).hasSize(1);
        Gig persistedGig = persistedGigs.get(0);
        assertThat(persistedGig.getStudentId()).isEqualTo(studentId);
        assertThat(persistedGig.getTierBasique().getPrice()).isEqualByComparingTo("10.00");
        assertThat(persistedGig.getTierStandard().getDescription()).isEqualTo("CRUD complet");
        assertThat(persistedGig.getTierPremium().getDeliveryDays()).isEqualTo(2);
        assertThat(persistedGig.isPublished()).isTrue();
        verify(identityClient).getStudentStatus(studentId);
    }

    @Test
    void createGigShouldRejectInvalidPayload() throws Exception {
        GigCreateRequest request = createRequest(UUID.randomUUID(), false);
        request.setTitle(" ");
        request.getTierBasique().setPrice(BigDecimal.ZERO);

        mockMvc.perform(post("/api/gigs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.title").exists())
                .andExpect(jsonPath("$.details['tierBasique.price']").exists());

        assertThat(gigRepository.findAll()).isEmpty();
        verifyNoInteractions(identityClient);
    }

    @Test
    void createPublishedGigShouldRejectUnverifiedStudent() throws Exception {
        UUID studentId = UUID.randomUUID();
        GigCreateRequest request = createRequest(studentId, true);
        when(identityClient.getStudentStatus(studentId)).thenReturn(new StudentStatusResponse(false, false));

        mockMvc.perform(post("/api/gigs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));

        assertThat(gigRepository.findAll()).isEmpty();
        verify(identityClient).getStudentStatus(studentId);
    }

    @Test
    void searchGigsShouldApplyCategoryAndLocationFiltersToPublishedGigs() throws Exception {
        UUID studentId = UUID.randomUUID();
        saveGig(studentId, "Logo express", "Design", "Paris", true);
        saveGig(studentId, "Logo lyonnais", "Design", "Lyon", true);
        saveGig(studentId, "Cours de maths", "Cours", "Paris", true);
        saveGig(studentId, "Draft logo", "Design", "Paris", false);

        mockMvc.perform(get("/api/gigs")
                        .param("category", "design")
                        .param("location", "par"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Logo express"))
                .andExpect(jsonPath("$[0].published").value(true));
    }

    @Test
    void getGigShouldReturnNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/api/gigs/{gigId}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void publishGigShouldVerifyStudentBeforePublishing() throws Exception {
        UUID studentId = UUID.randomUUID();
        Gig draft = saveGig(studentId, "API draft", "Developpement", "Paris", false);
        when(identityClient.getStudentStatus(studentId)).thenReturn(new StudentStatusResponse(true, false));

        mockMvc.perform(patch("/api/gigs/{gigId}/publish", draft.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.published").value(true));

        assertThat(gigRepository.findById(draft.getId())).get().extracting(Gig::isPublished).isEqualTo(true);
        verify(identityClient).getStudentStatus(studentId);
    }

    private Gig saveGig(UUID studentId, String title, String category, String location, boolean published) {
        return gigRepository.save(Gig.builder()
                .studentId(studentId)
                .title(title)
                .description("Description " + title)
                .category(category)
                .location(location)
                .rating(BigDecimal.ZERO)
                .tierBasique(tier("Basic", "Simple endpoint", "10.00", 7))
                .tierStandard(tier("Standard", "CRUD complet", "25.00", 5))
                .tierPremium(tier("Premium", "API documentee", "45.00", 2))
                .published(published)
                .build());
    }

    private GigCreateRequest createRequest(UUID studentId, boolean published) {
        return GigCreateRequest.builder()
                .studentId(studentId)
                .title("Developpement API")
                .description("Creation d'une API Spring Boot")
                .category("Developpement")
                .location("Paris")
                .tierBasique(tier("Basic", "Simple endpoint", "10.00", 7))
                .tierStandard(tier("Standard", "CRUD complet", "25.00", 5))
                .tierPremium(tier("Premium", "API documentee", "45.00", 2))
                .published(published)
                .build();
    }

    private GigTierDto tier(String title, String description, String price, int deliveryDays) {
        return GigTierDto.builder()
                .title(title)
                .description(description)
                .price(new BigDecimal(price))
                .deliveryDays(deliveryDays)
                .build();
    }
}
