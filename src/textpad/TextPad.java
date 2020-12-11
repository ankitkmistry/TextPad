package textpad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Ankit Kumar Mistry
 */

public class TextPad extends Application implements Initializable {
    
    @FXML
    private TextArea text; // Text Area    
       
    // MENU ITEM    
    // In File Menu
    
    @FXML
    private CheckMenuItem asave; // Auto Save
    
    @FXML
    private MenuItem sav; // Save
    
    // Format Menu
    
    @FXML
    private CheckMenuItem wrap; // Word Wrap
    
    // About
    
    @FXML
    private AnchorPane about;
    
    // Edit menu
    
    @FXML
    private MenuItem undoitem;
    
    @FXML
    private MenuItem redoitem;
    
    // Font Dialog Box
    
    @FXML
    private AnchorPane fontDialog;
    
    @FXML
    private ToggleButton bd; // Bold

    @FXML
    private Label size; // Size

    @FXML
    private ColorPicker pick; // Color picker

    @FXML
    private ToggleButton ul; // Underline

    @FXML
    private Slider sl; // Size slider

    @FXML
    private ToggleButton it; // Italic

    @FXML
    private ComboBox<String> font; // Font
    
    @FXML
    private Pane colorView; // Color View

    private ObservableList<String> fontList; // Font List
    
    boolean saved = false;
    
    boolean italic = false;
    
    boolean bold = false;
    
    boolean underline = false;
    
    String fontColor = "Consolas";
    
    File file;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FTextPad.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("textpad.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("textpadicon.png")));
        primaryStage.setTitle("Untitled - TextPad");
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }       
    
    @FXML
    public void close(ActionEvent ae){
        Platform.exit();
    }
    
    @FXML
    public void undo(ActionEvent ae){
        text.undo();
    }
    
    @FXML
    public void redo(ActionEvent ae){
        text.redo();
    }
    
    @FXML
    public void copy(ActionEvent ae){
        text.copy();
    }
    
    @FXML
    public void cut(ActionEvent ae){
        text.cut();
    }
    
    @FXML
    public void paste(ActionEvent ae){
        text.paste();
    }
    
    @FXML
    public void deselect(ActionEvent ae){
        text.deselect();
    }
    
    @FXML
    public void selectAll(ActionEvent ae){
        text.selectAll();
    }
    
    @FXML
    public void open(ActionEvent ae){
        if (saved || text.getText().equals("")){
            FileChooser chooser = new FileChooser();
            file = chooser.showOpenDialog(null);
            String txt = "";
            int c;
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(file);
                while((c = fin.read()) != -1){
                    char cha = (char) c;
                    txt += cha;
                }
            } catch (FileNotFoundException ex) {
                Alert error = new Alert(Alert.AlertType.ERROR, "File not found", ButtonType.OK);
                error.showAndWait();
            } catch (IOException ex){
                Alert error = new Alert(Alert.AlertType.ERROR, "Failed to read from the file", ButtonType.OK);
                error.showAndWait();
            }finally{
                try{
                    fin.close();
                } catch (IOException ex) {
                    Alert error = new Alert(Alert.AlertType.ERROR, "Failed to release resource", ButtonType.OK);
                    error.showAndWait();
                }
            }                   
            text.setText(txt);
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("TextPad");
            alert.setHeaderText("");
            alert.setContentText("Do you want to save??");
            alert.showAndWait();
            save(new ActionEvent());
            FileChooser chooser = new FileChooser();
            FileChooser.ExtensionFilter all = new FileChooser.ExtensionFilter("All Files", "*.*");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text file", "*.txt"),
                    all
            );
            chooser.setSelectedExtensionFilter(all);
            chooser.setTitle("Open");
            file = chooser.showOpenDialog(null);
            String txt = "";
            int c;
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(file);
                while((c = fin.read()) != -1){
                    char ch = (char) c;
                    txt += ch;
                }
            } catch (FileNotFoundException ex) {
                Alert error = new Alert(Alert.AlertType.ERROR, "File not found", ButtonType.OK);
                error.showAndWait();
            } catch (IOException ex){
                Alert error = new Alert(Alert.AlertType.ERROR, "Failed to read from the file", ButtonType.OK);
                error.showAndWait();
            }finally{
                try{
                    fin.close();
                } catch (IOException ex) {
                    Alert error = new Alert(Alert.AlertType.ERROR, "Failed to release resource", ButtonType.OK);
                    error.showAndWait();
                }
            }                   
            text.setText(txt);
        }
        saved = false;
        Stage stage = (Stage) text.getScene().getWindow();
        stage.setTitle(file.getName() + " - TextPad");
    }
    
    @FXML
    public void newFile(ActionEvent ae){
        if (saved || text.getText().equals("")){
            text.setText("");
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("TextPad");
            alert.setHeaderText("");
            alert.setContentText("Do you want to save??");
            alert.showAndWait();
            save(new ActionEvent());
            text.setText("");
        }
        saved = false;
        Stage stage = (Stage) text.getScene().getWindow();
        stage.setTitle("Untitled - TextPad");
    }
    
    @FXML
    public void save(ActionEvent ae){
        if(saved){
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(file);
                byte[] str = text.getText().getBytes();
                fout.write(str);
            /*} catch (FileNotFoundException ex) {*/
            } catch (IOException ex) {
                Alert error = new Alert(Alert.AlertType.ERROR, "Failed to write to the file", ButtonType.OK);
                error.showAndWait();
            } finally {
                try {
                    fout.close();
                } catch (IOException ex) {
                    Alert error = new Alert(Alert.AlertType.ERROR, "Failed to release resource", ButtonType.OK);
                    error.showAndWait();
                }
            }
        }else{
            FileOutputStream fout = null;
            try {
                FileChooser chooser = new FileChooser();
                FileChooser.ExtensionFilter all = new FileChooser.ExtensionFilter("Text file", "*.txt");
                chooser.getExtensionFilters().addAll(
                        all,
                        new FileChooser.ExtensionFilter("All Files", "*.*")
                );  
                chooser.setSelectedExtensionFilter(all);
                chooser.setTitle("Save");
                file = chooser.showSaveDialog(null);
                fout = new FileOutputStream(file);
                byte[] str = text.getText().getBytes();
                fout.write(str);
            } catch (FileNotFoundException ex) {
                Alert error = new Alert(Alert.AlertType.ERROR, "File not found", ButtonType.OK);
                error.showAndWait();
            } catch (IOException ex) {
                Alert error = new Alert(Alert.AlertType.ERROR, "Failed to write to the file", ButtonType.OK);
                error.showAndWait();
            } finally {
                try {
                    fout.close();
                } catch (IOException ex) {
                    Alert error = new Alert(Alert.AlertType.ERROR, "Failed to release resource", ButtonType.OK);
                    error.showAndWait();
                }
            }
            
        }
        saved = true;
        sav.setDisable(true);
        Stage stage = (Stage) text.getScene().getWindow();
        stage.setTitle(file.getName() + " - TextPad");
    }
    
    @FXML
    public void saveAs(ActionEvent ae){
        FileOutputStream fout = null;
        try {
            FileChooser chooser = new FileChooser();
            FileChooser.ExtensionFilter all = new FileChooser.ExtensionFilter("Text file", "*.txt");
            chooser.getExtensionFilters().addAll(
                    all,
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            chooser.setSelectedExtensionFilter(all);
            chooser.setTitle("Save As");
            file = chooser.showSaveDialog(null);
            fout = new FileOutputStream(file);
            byte[] str = text.getText().getBytes();
            fout.write(str);
        } catch (FileNotFoundException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR, "File not found", ButtonType.OK);
            error.showAndWait();
        } catch (IOException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Failed to write to the file", ButtonType.OK);
            error.showAndWait();
        } finally {
            try {
                fout.close();
            } catch (IOException ex) {
                Alert error = new Alert(Alert.AlertType.ERROR, "Failed to release resource", ButtonType.OK);
                error.showAndWait();
            }
        }
        saved = true;
        sav.setDisable(true);
        Stage stage = (Stage) text.getScene().getWindow();
        stage.setTitle(file.getName() + " - TextPad");
    }
    
    @FXML
    public void autoSave(ActionEvent ae){
        ChangeListener<String> saveMecahanism = (obVal, oldVal, newVal) -> {
                save(new ActionEvent());
        };
        if(asave.isSelected()){
            text.textProperty().addListener(saveMecahanism);
        }else{
            text.textProperty().removeListener(saveMecahanism);
        }
    }
    
    @FXML
    public void wordWrap(ActionEvent ae){
        if(wrap.isSelected()){
            if(!text.isWrapText()){
               text.setWrapText(true);
            }
        }else{
            if(text.isWrapText()){
               text.setWrapText(false);
            }
        }
    }
    
    @FXML
    public void about(ActionEvent ae) throws Exception{
        about.setVisible(!about.isVisible());
    }
    
    @FXML
    public void openFontDialog(ActionEvent ae) throws Exception{
        if(fontDialog.isVisible())
            fontDialog.setVisible(false);
        else{
            fontDialog.setVisible(true);
            sl.valueProperty().addListener((obVal, oldVal, newVal) -> {
                size.setText(String.valueOf(newVal.intValue()));
                Font textFont = text.getFont();
                if(italic){
                    if (bold){
                        text.setFont(Font.font(textFont.getName(), FontWeight.BOLD, FontPosture.ITALIC, newVal.doubleValue()));                        
                    }else{
                        text.setFont(Font.font(textFont.getName(), FontWeight.NORMAL, FontPosture.ITALIC, newVal.doubleValue()));                        
                    }
                }else{
                    if (bold){
                        text.setFont(Font.font(textFont.getName(), FontWeight.BOLD, FontPosture.REGULAR, newVal.doubleValue()));                        
                    }else{
                        text.setFont(Font.font(textFont.getName(), FontWeight.NORMAL, FontPosture.REGULAR, newVal.doubleValue()));                        
                    }
                }
            });
            
            if(bold)
                bd.setSelected(true);
            else
                bd.setSelected(false);
            
            if(italic)
                it.setSelected(true);
            else
                it.setSelected(false);
            
            if(underline)
                ul.setSelected(true);
            else
                ul.setSelected(false);
            
            font.setItems(fontList);
            font.setValue(text.getFont().getName());
            pick.setValue(Color.web(fontColor));
            colorView.setStyle("-fx-background-color: " + fontColor + ";");
        }
    }
    
    
    @FXML
    public void fontApply(ActionEvent ae){
        String color = code(pick.getValue());
        String fontval = font.getValue();
        double sizeval = sl.getValue();
        FontPosture posture;
        FontWeight weight;
        fontColor = color;
        text.setStyle(text.getStyle() + "-fx-text-fill: " + color + ";");
        size.setText("" + (int) sizeval);
        if (bd.isSelected()){ // Is Bold
            weight = FontWeight.BOLD;
        }else{
            weight = FontWeight.NORMAL;
        }
        if(it.isSelected()){ // Is Italic
            posture = FontPosture.ITALIC;
            italic = true;
        }else{
            posture = FontPosture.REGULAR;
            italic = false;
        }
        text.setFont(Font.font(fontval, weight, posture, sizeval));
        if(ul.isSelected()){ // Is Underline
            text.getStyleClass().remove("textare1");
            text.getStyleClass().add("textare");
            underline = true;
        }else{
            text.getStyleClass().remove("textare");
            text.getStyleClass().add("textare1");
            underline = false;
        }
        colorView.setStyle("-fx-background-color: " + color + ";");
        text.applyCss();
        colorView.applyCss();
    }
    
    public static String code( Color color ){
        return String.format(
            "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 )
        );
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pick.setValue(Color.BLACK);
        text.textProperty().addListener((obVal, oldVal, newVal) -> {
            if(text.getText().isEmpty())
                sav.setDisable(true);
            else
                sav.setDisable(false);
        });
        fontList = FXCollections.observableArrayList(Font.getFontNames());
    }       
}