package com.example.projet_java;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class dashboardcontroll implements Initializable {

    @FXML
    private TableView<livreurbd> table_veiw_liv;
    @FXML
    private AnchorPane tab_com;
    @FXML
    private AnchorPane tab_pro;
    @FXML
    private AnchorPane main_form;
    @FXML
    private Button livreur;
    @FXML
    private Button produit;
    @FXML
    private Button commande;
    @FXML
    private Button log_out;
    @FXML
    private Button close_dash;
    @FXML
    private Button mini_dash;
    @FXML
    private AnchorPane tab_livr;
    @FXML
    private TableColumn<livreurbd, String> id_livreur;
    @FXML
    private TableColumn<livreurbd, String> nom_liv;
    @FXML
    private TableColumn<livreurbd, String> prenom_liv;
    @FXML
    private TableColumn<livreurbd, String> tele_liv;
    @FXML
    private Button add_liv;
    @FXML
    private TextField pre_liv_add;
    @FXML
    private TextField id_liv_add;
    @FXML
    private TextField nom_liv_add;
    @FXML
    private TextField tele_add;
    @FXML
    private Button update_liv;
    @FXML
    private Button del_liv;

    public void close(){
        System.exit(0);
    }
    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    private double x = 0;
    private double y = 0;

    // switch between les scenes de dashboard
    public void switchForm(ActionEvent event) {

        if (event.getSource() == livreur) {
            tab_livr.setVisible(true);
            tab_pro.setVisible(false);
            tab_com.setVisible(false);

        } else if (event.getSource() == produit) {
            tab_livr.setVisible(false);
            tab_pro.setVisible(true);
           tab_com.setVisible(false);

        } else if (event.getSource() == commande) {
            tab_livr.setVisible(false);
            tab_pro.setVisible(false);
            tab_com.setVisible(true);
        }

    }

    // Log out of the dashboard
    public void logout() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {

                log_out.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    public ObservableList<livreurbd> addlivreurListData() {

        ObservableList<livreurbd> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM livreur";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            livreurbd livreur;

            while (result.next()) {
                livreur = new livreurbd(result.getInt("id_admin"),
                        result.getString("nom"),
                        result.getString("prenom"),
                        result.getInt("tele"));

                listData.add(livreur);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<livreurbd> addlivreurList;

    public void addlivreurShowListData() {
        addlivreurList = addlivreurListData();

        id_livreur.setCellValueFactory(new PropertyValueFactory<>("id_admin"));
       nom_liv.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom_liv.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        tele_liv.setCellValueFactory(new PropertyValueFactory<>("tele"));

        table_veiw_liv.setItems(addlivreurList);

    }
    public void addlivreurSelect() {
        livreurbd livreur = table_veiw_liv.getSelectionModel().getSelectedItem();
        int num = table_veiw_liv.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        id_liv_add.setText(String.valueOf(livreur.getId_admin()));
        nom_liv_add.setText(livreur.getNom());
        pre_liv_add.setText(livreur.getPrenom());
        tele_add.setText(String.valueOf(livreur.getTele()));

    }
// add into livreur table
    public void addlivreurAdd() {

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO livreur "
                + "('id_admin','name','prenom','tele') "
                + "VALUES(?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;
            if (id_liv_add.getText().isEmpty()
                    || nom_liv_add.getText().isEmpty()
                    || pre_liv_add.getText().isEmpty()
                    || tele_add.getText().isEmpty()
                    || getData.path == null || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();
            } else {

                String check = "SELECT id_admin FROM livreur WHERE id_admin = '"
                        + id_liv_add.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(check);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("livreur ID: " + id_liv_add.getText() + " was already exist!");
                    alert.showAndWait();
                } else {

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(2, id_liv_add.getText());
                    prepare.setString(3, nom_liv_add.getText());
                    prepare.setString(4, pre_liv_add.getText());
                    prepare.setString(5, tele_add.getText());


                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");

                    prepare.setString(6, uri);
                    prepare.setString(7, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    String insertInfo = "INSERT INTO livreur "
                            + "(id_admin,name,prenom,tele) "
                            + "VALUES(?,?,?,?,?,?,?)";

                    prepare = connect.prepareStatement(insertInfo);
                    prepare.setString(2, id_liv_add.getText());
                    prepare.setString(3, nom_liv_add.getText());
                    prepare.setString(4, pre_liv_add.getText());
                    prepare.setString(5, tele_add.getText());
                    prepare.setString(6, "0.0");
                    prepare.setString(7, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    addlivreurShowListData();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addlivreurShowListData();

    }
}
