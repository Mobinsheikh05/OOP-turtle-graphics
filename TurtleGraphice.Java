import uk.ac.leedsbeckett.oop.LBUGraphics;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TurtleGraphics extends LBUGraphics {

    private List<String> commandHistory = new ArrayList<>();
    private boolean isImageSaved = true;
    private boolean isCommandsSaved = true;

    public TurtleGraphics() {
        setPenColour(Color.BLACK);
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

                case "saveimage": saveImage(); break;
                case "loadimage": loadImage(); break;
                case "savecommands": saveCommands(); break;
                case "loadcommands": loadCommands(); break;

                default:
                    displayMessage("Invalid command: " + command);
                    return;
            }

            // Track command history if it is a drawing command
            if (!cmd.equals("saveimage") && !cmd.equals("loadimage") && !cmd.equals("savecommands") && !cmd.equals("loadcommands")) {
                commandHistory.add(command);
                isImageSaved = false;
                isCommandsSaved = false;
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

    // --- Save the current canvas as an image ---
    private void saveImage() {
        try {
            BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            paint(img.getGraphics());
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File outFile = fileChooser.getSelectedFile();
                ImageIO.write(img, "png", outFile);
                isImageSaved = true;
                displayMessage("Image saved successfully.");
            }
        } catch (IOException e) {
            displayMessage("Failed to save image.");
        }
    }

    // --- Load an image and display it ---
    private void loadImage() {
        if (!isImageSaved) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Current image is not saved. Do you want to save it first?",
                    "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == JOptionPane.CANCEL_OPTION) return;
            if (choice == JOptionPane.YES_OPTION) saveImage();
        }

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage img = ImageIO.read(fileChooser.getSelectedFile());
                getGraphics().drawImage(img, 0, 0, null);
                isImageSaved = true;
                displayMessage("Image loaded successfully.");
            } catch (IOException e) {
                displayMessage("Failed to load image.");
            }
        }
    }

    // --- Save all typed commands to a text file ---
    private void saveCommands() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                for (String cmd : commandHistory) {
                    writer.println(cmd);
                }
                isCommandsSaved = true;
                displayMessage("Commands saved.");
            } catch (IOException e) {
                displayMessage("Failed to save commands.");
            }
        }
    }

    // --- Load and execute all commands from a text file ---
    private void loadCommands() {
        if (!isCommandsSaved) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Current commands are not saved. Do you want to save them first?",
                    "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == JOptionPane.CANCEL_OPTION) return;
            if (choice == JOptionPane.YES_OPTION) saveCommands();
        }

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    processCommand(line);  // Reuse existing command processor
                }
                displayMessage("Commands loaded and executed.");
            } catch (IOException e) {
                displayMessage("Failed to load commands.");
            }
        }
    }
}
