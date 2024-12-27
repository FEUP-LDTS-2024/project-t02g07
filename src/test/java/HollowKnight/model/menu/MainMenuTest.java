package HollowKnight.model.menu;

import HollowKnight.model.dataStructs.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MainMenuTest {

    @Mock
    private Option startOption; // Mock the Option class to test selection and navigation
    @Mock
    private Option settingsOption;
    @Mock
    private Option exitOption;

    private MainMenu mainMenu;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
        mainMenu = new MainMenu();  // Initialize MainMenu
    }

    @Test
    void testNextOption_shouldCycleOptionsCorrectly() {
        // Arrange: Set initial selection
        mainMenu.nextOption(); // Go to "Settings"

        // Act & Assert: Test next selection works correctly
        assertEquals(mainMenu.getCurrentOption().getType(), Option.Type.SETTINGS);

        // Act: Going to the next one ("Exit")
        mainMenu.nextOption();

        // Assert: Verify we moved to "Exit"
        assertEquals(mainMenu.getCurrentOption().getType(), Option.Type.EXIT);

        // Act: Going beyond the last option should cycle back to "Start"
        mainMenu.nextOption();

        // Assert: Verify we cycled back to "Start"
        assertEquals(mainMenu.getCurrentOption().getType(), Option.Type.START_GAME);
    }

    @Test
    void testPreviousOption_shouldCycleBackwardsCorrectly() {
        // Arrange: Set initial selection
        mainMenu.nextOption(); // Go to "Settings"
        mainMenu.nextOption(); // Now at "Exit"

        // Act & Assert: Go backward to "Settings"
        mainMenu.previousOption();
        assertEquals(mainMenu.getCurrentOption().getType(), Option.Type.SETTINGS);

        // Act: Going back again to "Start"
        mainMenu.previousOption();

        // Assert: We should be back at "Start"
        assertEquals(mainMenu.getCurrentOption().getType(), Option.Type.START_GAME);

        // Act: Going past the first option should cycle back to "Exit"
        mainMenu.previousOption();

        // Assert: Verify we cycled back to "Exit"
        assertEquals(mainMenu.getCurrentOption().getType(), Option.Type.EXIT);
    }

    @Test
    void testIsSelected_shouldReturnCorrectlyForOptions() {
        // Act: Go to "Settings"
        mainMenu.nextOption();

        // Assert: Check if "Settings" is selected
        assertTrue(mainMenu.isSelected(1)); // The settings option should be selected (index 1)
        assertFalse(mainMenu.isSelected(0)); // "Start" should not be selected
        assertFalse(mainMenu.isSelected(2)); // "Exit" should not be selected

        // Act: Go to "Exit"
        mainMenu.nextOption();

        // Assert: Check if "Exit" is selected
        assertTrue(mainMenu.isSelected(2)); // "Exit" should be selected
    }

    @Test
    void testIsSelectedExit_shouldReturnTrueForExit() {
        // Act: Set the selection to "Exit"
        mainMenu.nextOption(); // "Settings"
        mainMenu.nextOption(); // "Exit"

        // Assert: Test the isSelectedExit() function
        assertTrue(mainMenu.isSelectedExit());
    }

    @Test
    void testIsSelectedStart_shouldReturnTrueForStart() {
        // Act: Test the "Start" option is selected by default (on initialization, should be index 0)
        assertTrue(mainMenu.isSelectedStart());
    }

    @Test
    void testGetParticles_shouldReturnListOfParticles() {
        // Act
        List<Particle> particles = mainMenu.getParticles();

        // Assert: Ensure particles list is not null and contains expected number of particles
        assertNotNull(particles);
        assertEquals(250, particles.size());  // Should match size set in MainMenu (250)
    }

    @Test
    void testGetOptions_shouldReturnOptionsList() {
        // Act
        List<Option> options = mainMenu.getOptions();

        // Assert: Verify the list has the expected options (Start, Settings, Exit)
        assertNotNull(options);
        assertEquals(3, options.size());

        // Option types should match
        assertEquals(Option.Type.START_GAME, options.get(0).getType());
        assertEquals(Option.Type.SETTINGS, options.get(1).getType());
        assertEquals(Option.Type.EXIT, options.get(2).getType());
    }

    @Test
    void testGetInGame_shouldReturnDefaultValue() {
        // Arrange: Initialize MainMenu object (which has inGame as false by default)

        // Act: Check the initial value of inGame
        Boolean inGameStatus = mainMenu.getInGame();

        // Assert: Verify it returns false by default
        assertFalse(inGameStatus);
    }

    @Test
    void testSetInGame_shouldSetValueCorrectly() {
        // Act: Set inGame value to true
        mainMenu.setInGame(true);

        // Assert: Verify that inGame has been updated
        assertTrue(mainMenu.getInGame());

        // Act: Set inGame value to false
        mainMenu.setInGame(false);

        // Assert: Verify that inGame has been updated back to false
        assertFalse(mainMenu.getInGame());
    }

    @Test
    void testOptionGetType_shouldReturnCorrectType() {
        // Arrange: Create specific options to test their type.
        Option start = new Option(30, 25, Option.Type.START_GAME);
        Option settings = new Option(30, 31, Option.Type.SETTINGS);
        Option exit = new Option(30, 37, Option.Type.EXIT);

        // Act & Assert: Verify that the type for each option is correct
        assertEquals(Option.Type.START_GAME, start.getType());
        assertEquals(Option.Type.SETTINGS, settings.getType());
        assertEquals(Option.Type.EXIT, exit.getType());
        assertEquals(new Position(30, 25), start.getPosition());
    }
}