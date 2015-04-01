package game;

public class HScrollbar {
    float barWidth; //Bar's width in pixels
    float barHeight; //Bar's height in pixels
    float xPosition; //Bar's x location in pixels
    float yPosition; //Bar's y location in pixels
    float sliderPosition, newSliderPosition; //Position of slider
    float sliderPositionMin, sliderPositionMax; //Max and min values of slider
    boolean mouseOver; //Is the mouse over the slider?
    boolean locked; //Is the mouse clicking and dragging the slider now?
    boolean ready;

    Game parent;

    /**
     * @param x The x location of the top left corner of the bar in pixels
     * @param y The y location of the top left corner of the bar in pixels
     * @param w The width of the bar in pixels
     * @param h The height of the bar in pixels
     * @brief Creates a new horizontal scrollbar
     */
    public HScrollbar(float x, float y, float w, float h, Game parent) {
        barWidth = w;
        barHeight = h;
        xPosition = x;
        yPosition = y;
        sliderPosition = xPosition + barWidth / 2 - barHeight / 2;
        newSliderPosition = sliderPosition;
        sliderPositionMin = xPosition;
        sliderPositionMax = xPosition + barWidth - barHeight;
        this.parent = parent;
    }

    /**
     * @brief Updates the state of the scrollbar according to the mouse movement
     */
    void update() {
        if (isMouseOver()) {
            mouseOver = true;
        } else {
            mouseOver = false;
        }
        if (!parent.mousePressed && mouseOver) {
            ready = true;
        }
        if (ready && parent.mousePressed && mouseOver) {
            locked = true;
        } else if (parent.mousePressed && !locked) {
            ready = false;
        }
        if (!parent.mousePressed) {
            locked = false;
        }
        if (locked) {
            newSliderPosition = constrain(parent.mouseX - barHeight / 2, sliderPositionMin, sliderPositionMax);
        }
        if (parent.abs(newSliderPosition - sliderPosition) > 1) {
            sliderPosition = sliderPosition + (newSliderPosition - sliderPosition);
        }
    }

    /**
     * @param val    The value to be clamped
     * @param minVal Smallest value possible
     * @param maxVal Largest value possible
     * @return val clamped into the interval [minVal, maxVal]
     * @brief Clamps the value into the interval
     */
    float constrain(float val, float minVal, float maxVal) {
        return parent.min(parent.max(val, minVal), maxVal);
    }

    /**
     * @return Whether the mouse is hovering the scrollbar
     * @brief Gets whether the mouse is hovering the scrollbar
     */
    boolean isMouseOver() {
        if (parent.mouseX > xPosition && parent.mouseX < xPosition + barWidth &&
                parent.mouseY > yPosition && parent.mouseY < yPosition + barHeight) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @brief Draws the scrollbar in its current state
     */
    void display() {
        parent.noStroke();
        parent.fill(204);
        parent.rect(xPosition, yPosition, barWidth, barHeight);
        if (mouseOver || locked) {
            parent.fill(0, 0, 0);
        } else {
            parent.fill(102, 102, 102);
        }
        parent.rect(sliderPosition, yPosition, barHeight, barHeight);
    }

    /**
     * @return The slider location in the interval [0,1]
     * corresponding to [leftmost location, rightmost location]
     * @brief Gets the slider location
     */
    float getPos() {
        return (sliderPosition - xPosition) / (barWidth - barHeight);
    }

    float getClampedPos(){
        return constrain(getPos()*2, 0.1f, 2);
    }
}

