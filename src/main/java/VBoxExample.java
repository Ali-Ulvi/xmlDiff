import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.custommonkey.xmlunit.XMLUnit;

import java.io.*;

public class VBoxExample extends Application {
    File Control, Test;
    public Label ll;
    Stage stg;
    String path, sonuc, lastDir;
    CheckBox ignoreComment, ignoreAtributeOrder, ignoreElementOrder,ignoreWhiteSpace;

    @Override
    public void start(Stage stage) {
// Buttons
        stg = stage;
        Button btn1 = new Button("Choose Control File (Dosya 1)");
        Button btn2 = new Button("Choose Test File (Dosya 2)");
        Button btn3 = new Button(" TEST ! ");
        Button btn4 = new Button("Copy Result to Clipboard");
        btn2.setOnAction(new SingleFcButtonListener2());
        btn3.setOnAction(new diff());
        btn4.setOnAction(new copy());
        btn1.setOnAction(new SingleFcButtonListener());
        HBox buttonHb1 = new HBox(10);
        ignoreAtributeOrder =new CheckBox("ignoreAtributeOrder");
        ignoreComment =new CheckBox("ignoreComments");
        ignoreElementOrder =new CheckBox("ignoreElementOrder");
        ignoreWhiteSpace =new CheckBox("ignoreWhiteSpace");
        ignoreElementOrder.setSelected(true);
        ignoreWhiteSpace.setSelected(true);
        buttonHb1.setAlignment(Pos.CENTER);
        buttonHb1.getChildren().addAll(btn1, btn2, btn3, ignoreComment, ignoreAtributeOrder, ignoreElementOrder,ignoreWhiteSpace,btn4);
        buttonHb1.setMargin(btn1, new Insets(20, 10, 0, 10));
        buttonHb1.setMargin(btn2, new Insets(20, 10, 0, 10));
        buttonHb1.setMargin(btn3, new Insets(20, 10, 0, 10));
        buttonHb1.setMargin(btn4, new Insets(20, 20, 0, 45));

        ScrollPane l = new ScrollPane();
        ll = new Label();
        ll.setWrapText(true);
        ll.setTextAlignment(TextAlignment.JUSTIFY);
        stage.setMaximized(true);
        l.setContent(ll);

        //Instantiating the VBox class
        VBox vBox = new VBox();

        //Setting the space between the nodes of a VBox pane
        //vBox.setSpacing(10);


        //retrieving the observable list of the VBox
        ObservableList list = vBox.getChildren();

        //Adding all the nodes to the observable list
        list.addAll(buttonHb1, l);
        //Setting the margin to the nodes
        vBox.setMargin(l, new Insets(10, 10, 10, 10));

        l.setPrefViewportWidth(1720);
        l.setPrefViewportHeight(620);
        //Creating a scene object
        Scene scene = new Scene(vBox);

        //Setting title to the Stage
        stage.setTitle("XML Equality Tester Pro By AUT");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
        try (BufferedReader br = new BufferedReader(new FileReader("lastDirAUT.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lastDir = line;
                break;
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private class SingleFcButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            showSingleFileChooser();
        }
    }

    private class SingleFcButtonListener2 implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            showSingleFileChooser2();
        }
    }

    private class diff implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            try {
                sonuc = new MyXMLTestCase("AUTCase").testRepeatedChildElements(Control, Test, VBoxExample.this);

            } catch (Exception e1) {
                ll.setText(e1.getMessage());
            }
        }
    }

    private class copy implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            try {

                ClipboardContent content = new ClipboardContent();
                content.putString("\n" + Control + ", " + Test + " \n\n" + sonuc);
                Clipboard.getSystemClipboard().setContent(content);
            } catch (Exception e1) {
                ll.setText(e1.getMessage());
            }
        }
    }

    private void showSingleFileChooser() {

        FileChooser fileChooser = new FileChooser();


        if (Control != null) {
            fileChooser.setInitialDirectory(Control.getParentFile());
            setLastDir(Control.getParent());
        } else if (Test != null) {
            fileChooser.setInitialDirectory(Test.getParentFile());
            setLastDir(Test.getParent());
        } else if (lastDir != null) {
            fileChooser.setInitialDirectory(new File(lastDir));
        }
        File selectedFile = fileChooser.showOpenDialog(stg);
        if (selectedFile != null) {

            Control = selectedFile;
            fileChooser.setInitialDirectory(Control.getParentFile());
        }

    }

    public void setLastDir(String lastDir) {
        this.lastDir = lastDir;
        FileWriter fw = null;
        try {
            fw = new FileWriter("lastDirAUT.txt");
            fw.write(lastDir);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSingleFileChooser2() {

        FileChooser fileChooser = new FileChooser();

        if (Test != null) {
            fileChooser.setInitialDirectory(Test.getParentFile());
            setLastDir(Test.getParent());
        } else if (Control != null) {
            fileChooser.setInitialDirectory(Control.getParentFile());
            setLastDir(Control.getParent());
        } else if (lastDir != null) {
            fileChooser.setInitialDirectory(new File(lastDir));
        }
        File selectedFile = fileChooser.showOpenDialog(stg);
        if (selectedFile != null) {

            Test = selectedFile;
        }

    }

    public static void main(String args[]) throws Exception {
        if (args.length == 0)
            launch(args);
        else if (args.length == 2) {
            new MyXMLTestCase("AUT Case").testRepeatedChildElements(args[0], args[1]);
            System.exit(0);
        }
        else if (args.length == 6) {
            new MyXMLTestCase("AUT Case").testRepeatedChildElements(args );
            System.exit(0);
        }
        else{
            System.out.println("options order: setIgnoreWhitespace setIgnoreComments setIgnoreAttributeOrder setIgnoreTagOrder");
            System.out.println("example: 1 1 0 1");
            System.exit(0);
        }

    }
}