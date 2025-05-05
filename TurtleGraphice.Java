import uk.ac.leedsbeckett.oop.LBUGraphics;
import java.awt.*;
import javax.swing.*;

public class TurtleGraphics extends LBUGraphics {

    public TurtleGraphics() {
        // Skip image loading and use simple graphics
        // You can set a default color for the turtle or use basic shapes for visualization
        setPenColour(Color.BLACK); // Default pen color
        setTurtleShape(); // Optionally use a simple shape for the turtle
    }

    // Override this method if needed, to provide a simple shape or drawing for the turtle
    private void setTurtleShape() {
        // You can customize this method to draw a simple shape instead of an image
        // For example, drawing a small rectangle or circle to represent the turtle
        Graphics g = getGraphics();
        if (g != null) {
            g.setColor(Color.RED); // Set turtle color to red (or any color you prefer)
            g.fillOval(0, 0, 10, 10); // Draw a small circle as the turtle
        }
    }


    public void processCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            displayMessage("No command entered.");
            return;
        }

        String[] parts = command.trim().split("\\s+");
        String cmd = parts[0].toLowerCase();

        try {
            switch (cmd) {
                case "about": about(); break;
                case "penup": drawOff(); break;
                case "pendown": drawOn(); break;
                case "left": left(); break;
                case "right": right(); break;
                case "move":
                    if (parts.length < 2) {
                        displayMessage("Missing distance for move.");
                        return;
                    }
                    int moveDistance = Integer.parseInt(parts[1]);
                    if (!isValidDistance(moveDistance)) {
                        displayMessage("Move distance must be between 1 and 1000.");
                        return;
                    }
                    forward(moveDistance);
                    break;
                case "reverse":
                    if (parts.length < 2) {
                        displayMessage("Missing distance for reverse.");
                        return;
                    }
                    int reverseDistance = Integer.parseInt(parts[1]);
                    if (!isValidDistance(reverseDistance)) {
                        displayMessage("Reverse distance must be between 1 and 1000.");
                        return;
                    }
                    forward(-reverseDistance);
                    break;
                case "black": setPenColour(Color.black); break;
                case "red": setPenColour(Color.red); break;
                case "green": setPenColour(Color.green); break;
                case "white": setPenColour(Color.white); break;
                case "reset": reset(); break;
                case "clear": clear(); break;
                default:
                    displayMessage("Invalid command: " + command);
                    break;
            }
        } catch (NumberFormatException e) {
            displayMessage("Invalid number format in command: " + command);
        }
    }

    private boolean isValidDistance(int distance) {
        return distance >= 1 && distance <= 1000;
    }

    @Override
    public void about() {
        super.about();
        displayMessage("Design by Mobin Sheikh.");
    }
}
