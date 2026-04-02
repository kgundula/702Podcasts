package defensivethinking.co.za.a702podcasts.utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecylerViewDividerItemDecorationTest {

    private MockedStatic<ContextCompat> contextCompatMockedStatic;

    @Before
    public void setUp() {
        contextCompatMockedStatic = Mockito.mockStatic(ContextCompat.class);
    }

    @After
    public void tearDown() {
        if (contextCompatMockedStatic != null) {
            contextCompatMockedStatic.close();
        }
    }

    @Test
    public void testOnDrawOver_PerformanceBaseline() {
        // Arrange
        Context mockContext = mock(Context.class);
        Drawable mockDrawable = mock(Drawable.class);
        when(mockDrawable.getIntrinsicHeight()).thenReturn(10);

        contextCompatMockedStatic.when(() -> ContextCompat.getDrawable(any(), any(Integer.class)))
                .thenReturn(mockDrawable);

        RecylerViewDividerItemDecoration decoration = new RecylerViewDividerItemDecoration(mockContext);

        Canvas mockCanvas = mock(Canvas.class);
        RecyclerView mockRecyclerView = mock(RecyclerView.class);
        RecyclerView.State mockState = mock(RecyclerView.State.class);

        int childCount = 5;
        when(mockRecyclerView.getChildCount()).thenReturn(childCount);
        when(mockRecyclerView.getPaddingLeft()).thenReturn(0);
        when(mockRecyclerView.getPaddingRight()).thenReturn(0);
        when(mockRecyclerView.getWidth()).thenReturn(100);

        for (int i = 0; i < childCount; i++) {
            View mockChild = mock(View.class);
            RecyclerView.LayoutParams mockParams = mock(RecyclerView.LayoutParams.class);
            mockParams.bottomMargin = 5;
            when(mockChild.getLayoutParams()).thenReturn(mockParams);
            when(mockChild.getBottom()).thenReturn(i * 20);
            when(mockRecyclerView.getChildAt(i)).thenReturn(mockChild);
        }

        // Act
        decoration.onDrawOver(mockCanvas, mockRecyclerView, mockState);

        // Assert - The optimization means getIntrinsicHeight is called only 1 time
        verify(mockDrawable, times(1)).getIntrinsicHeight();
    }
}
