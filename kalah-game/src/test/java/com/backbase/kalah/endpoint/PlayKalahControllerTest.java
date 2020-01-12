package com.backbase.kalah.endpoint;

import com.backbase.kalah.endpoints.PlayKalahController;
import com.backbase.kalah.endpoints.util.ControllerUtils;
import com.backbase.kalah.records.GameStatus;
import com.backbase.kalah.repos.GameStatusRepo;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Pattern;

import static com.backbase.kalah.util.ServiceConstance.jsonToObject;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sachith Dickwella
 */
@WebMvcTest(PlayKalahController.class)
public class PlayKalahControllerTest {

    /**
     *
     */
    @Autowired
    private MockMvc mockMvc;
    /**
     *
     */
    @MockBean
    private GameStatusRepo repo;
    /**
     *
     */
    private static GameStatus gameStatus;

    /**
     *
     */
    @Order(1)
    @Test
    public void testCreateGame() throws Exception {
        var body = mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        gameStatus = jsonToObject(body, GameStatus.class);
        assertNotNull("Response GameStatus is null", gameStatus);
        assertTrue("Game instance's id is not larger than zero(0)", gameStatus.getId() > 0);

        var pattern = Pattern.compile("^(?:http|https)://[a-zA-Z:0-9]+/games/(\\d{6,})$");
        var matcher = pattern.matcher(gameStatus.getURL());
        assertTrue("Invalid URL", matcher.find());
        assertEquals("URL contains invalid id", (long) gameStatus.getId(), Long.parseLong(matcher.group(1)));
    }

    /**
     *
     */
    @Order(2)
    @Test
    public void testMakeMove() throws Exception {
        var makeMoveBody = mockMvc.perform(put(String.format("/games/%d/pits/1", gameStatus.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        gameStatus = jsonToObject(makeMoveBody, GameStatus.class);
        assertNotNull("Response GameStatus is null", gameStatus);

    }
}
