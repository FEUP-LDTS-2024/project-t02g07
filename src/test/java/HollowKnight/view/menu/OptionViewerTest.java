package HollowKnight.view.menu;

import HollowKnight.gui.GUI;
import HollowKnight.gui.RescalableGUI;
import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.menu.Option;
import HollowKnight.view.text.GameTextViewer;
import HollowKnight.view.text.TextViewer;
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OptionViewerTest {

    @Mock
    private TextViewer textViewer;

    @Mock
    private RescalableGUI gui;

    private OptionViewer optionViewer;

    @BeforeEach
    public void setup() {
        this.gui = mock(RescalableGUI.class);
        this.textViewer = mock(GameTextViewer.class);

        this.optionViewer = new OptionViewer(textViewer);
    }

    @Test
    public void drawTesting(){
        Option[] entries = new Option[5];
        for (int idx = 0; idx < Option.Type.values().length; idx++){
            entries[idx] = new Option(0, 10*idx, Option.Type.values()[idx]);
        }
        for (Option e : entries){
            optionViewer.draw(e, gui, new TextColor.RGB(0, 0, 0));
        }
        verify(textViewer, times(5))
                .draw(anyString(), anyDouble(), anyDouble(), eq(new TextColor.RGB(0, 0, 0)), any());
        verify(textViewer, times(0))
                .draw(eq(""), anyDouble(), anyDouble(), eq(new TextColor.RGB(0, 0, 0)), any());
    }

    @Test
    public void resolutionLabels(){
        Option option = new Option(0,0, Option.Type.RESOLUTION);
        for (RescalableGUI.ResolutionScale gr : RescalableGUI.ResolutionScale.values()){
            when(gui.getResolutionScale()).thenReturn(gr);
            optionViewer.draw(option, gui, new TextColor.RGB(0, 0, 0));
        }
        verify(textViewer, times(1))
                .draw(anyString(), anyDouble(), anyDouble(), eq(new TextColor.RGB(0, 0, 0)), any());
        verify(textViewer, times(0))
                .draw(eq(""), anyDouble(), anyDouble(), eq(new TextColor.RGB(0, 0, 0)), any());
    }
}
