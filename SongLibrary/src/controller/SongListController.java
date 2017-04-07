/*

    CS213
    Project 1: SongLibrary
    Michael Triano & Joseph Wolak

 */

package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.SongCollection;

public class SongListController {

    @FXML
    ListView<SongCollection.Song> songList;

    @FXML
    Text title, artist, year, album;

    @FXML
    TextField titleField, artistField, yearField, albumField;

    @FXML
    Button addButton, deleteButton, editButton, saveButton;

    public ObservableList<SongCollection.Song> obsSongList;

    SongCollection songCollection = new SongCollection();

    public void start(Stage primaryStage) {

        obsSongList = FXCollections.observableArrayList(songCollection.listOfSongs);

        songList.setItems(obsSongList);

        //Set each item in the ListView with the song's title
        songList.setCellFactory(new Callback<ListView<SongCollection.Song>, ListCell<SongCollection.Song>>() {
            @Override
            public ListCell<SongCollection.Song> call(ListView<SongCollection.Song> param) {
                ListCell<SongCollection.Song> cell = new ListCell<SongCollection.Song>() {
                    @Override
                    protected void updateItem(SongCollection.Song item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null) {
                            setText(item.getTitle());
                        }
                        else {
                            setText("");
                        }
                    }

                };

                return cell;
            }
        });

        //Selects first item in list as default
        if(SongCollection.listOfSongs.size() > 0) {
            songList.getSelectionModel().select(0);
            setSongDetails();
            saveButton.setDisable(true);
        }

        //No songs in the list, want to disable buttons
        else {
            editButton.setDisable(true);
            saveButton.setDisable(true);
            deleteButton.setDisable(true);
        }

        //List view listener
        songList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> setSongDetails());

        //Add button listener
        addButton.setOnAction((event) -> {
            addSong();
        });

        //Delete button listener
        deleteButton.setOnAction((event) -> {
            deleteSong();
        });

        //Edit button listener
        editButton.setOnAction((event) -> {
            editSong();
        });

        //Save button listener
        saveButton.setOnAction((event) -> {
            saveSong();
        });


    }


    /*
        -------------------------

        Method: setSongDetails()

        Listener for list view
        Gets the selected Song from the list and displays the details for that song

        -------------------------
     */


    private void setSongDetails() {
        int index = songList.getSelectionModel().getSelectedIndex();

        //Fixes out of array index bug
        if(index < 0) return;

        System.out.println("Clicked on index: " + index);

        //Refreshes the ListView
        songList.refresh();

        title.setText(SongCollection.listOfSongs.get(index).getTitle());
        titleField.setText(SongCollection.listOfSongs.get(index).getTitle());
        artist.setText(SongCollection.listOfSongs.get(index).getArtist());
        artistField.setText(SongCollection.listOfSongs.get(index).getArtist());
        year.setText(SongCollection.listOfSongs.get(index).getYear());
        yearField.setText(SongCollection.listOfSongs.get(index).getYear());
        album.setText(SongCollection.listOfSongs.get(index).getAlbum());
        albumField.setText(SongCollection.listOfSongs.get(index).getAlbum());
    }


    /*
        -------------------------

        Method: addSong()

        Listener for add button
        Creates a new Song, adds it to the list and enables the user to fill in details

        -------------------------
     */

    private void addSong() {
        SongCollection.listOfSongs.add(new SongCollection.Song("New Song", "", "", ""));
        System.out.println(SongCollection.listOfSongs.size());

        //Refreshes the ListView
        obsSongList.removeAll(obsSongList);
        for(int i = 0; i < SongCollection.listOfSongs.size(); i++) {
            obsSongList.add(SongCollection.listOfSongs.get(i));

        }

        //Selects the last song in the list, which is the newly added song
        songList.getSelectionModel().select(obsSongList.size() - 1);

        //Enable editing
        editSong();

    }

    /*
        -------------------------

        Method: deleteSong()

        Listener for delete button
        Removes selected song from the list

        -------------------------
     */

    private void deleteSong() {
        int index = songList.getSelectionModel().getSelectedIndex();

        if(index < 0) return;

        SongCollection.listOfSongs.remove(index);

        //Refreshes the ListView
        obsSongList.removeAll(obsSongList);
        for(int i = 0; i < SongCollection.listOfSongs.size(); i++) {
            obsSongList.add(SongCollection.listOfSongs.get(i));

        }

        //List not empty, can reset list view
        if(SongCollection.listOfSongs.size() > 0) songList.getSelectionModel().select(0);

        //Empty list, disable appropriate buttons and set song details to blank
        else {
            editButton.setDisable(true);
            saveButton.setDisable(true);
            deleteButton.setDisable(true);

            title.setText("");
            titleField.setText("");
            artist.setText("");
            artistField.setText("");
            year.setText("");
            yearField.setText("");
            album.setText("");
            albumField.setText("");

        }

        SongCollection.exportToFile();
    }

    /*
        -------------------------

        Method: editSong()

        Listener for edit button
        Enables user to edit details of song

        -------------------------
     */

    private void editSong() {

        //Disable/enable appropriate buttons
        songList.setDisable(true);
        addButton.setDisable(true);
        deleteButton.setDisable(true);
        editButton.setDisable(true);
        saveButton.setDisable(false);

        invertFields();
    }

    /*
        -------------------------

        Method: saveSong()

        Listener for save button
        Gets the user input and saves data to Song object

        -------------------------
     */

    private void saveSong() {
        int index = songList.getSelectionModel().getSelectedIndex();

        //List empty
        if(index < 0) index = 0;


        //Warning for if any of the text fields about the song aren't filled
        if("".equals(titleField.getText()) || "".equals(artistField.getText())){
            Alert alert =
                    new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Information about the song is still missing!");
            alert.showAndWait();

            return;
        }
        

        //Check if song was changed
        //if(!title.getText().equals(titleField.getText()) || !album.getText().equals((albumField.getText()))) {
            //Warning for if a song shares the same name and artist
            for(SongCollection.Song s : SongCollection.listOfSongs){
                if(s.getTitle().equals(titleField.getText()) && s.getArtist().equals(artistField.getText())){
                    Alert alert =
                            new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("A song with this same title and artist already exists.");
                    alert.showAndWait();
                    return;
                }
            }
        //}


        invertFields();

        SongCollection.listOfSongs.get(index).setTitle(titleField.getText());
        SongCollection.listOfSongs.get(index).setArtist(artistField.getText());
        SongCollection.listOfSongs.get(index).setAlbum(albumField.getText());
        SongCollection.listOfSongs.get(index).setYear(yearField.getText());

        songList.setDisable(false);
        addButton.setDisable(false);
        deleteButton.setDisable(false);
        editButton.setDisable(false);
        saveButton.setDisable(true);

        //Refreshes the ListView
        SongCollection.sortList();
        obsSongList.removeAll(obsSongList);
        for(int i = 0; i < SongCollection.listOfSongs.size(); i++) {
            obsSongList.add(SongCollection.listOfSongs.get(i));
        }


        songList.getSelectionModel().select(index);
        SongCollection.exportToFile();
        setSongDetails();
    }

    /*
        -------------------------

        Method: invertFields()

        Inverts the visibility of appropriate objects

        -------------------------
    */

    private void invertFields() {
        if(title.isVisible() == true) title.setVisible(false);
        else title.setVisible(true);

        if(titleField.isVisible() == true) titleField.setVisible(false);
        else titleField.setVisible(true);

        if(artist.isVisible() == true)artist.setVisible(false);
        else artist.setVisible(true);

        if(artistField.isVisible() == true)artistField.setVisible(false);
        else artistField.setVisible(true);

        if(album.isVisible() == true)album.setVisible(false);
        else album.setVisible(true);

        if(albumField.isVisible() == true)albumField.setVisible(false);
        else albumField.setVisible(true);

        if(year.isVisible() == true)year.setVisible(false);
        else year.setVisible(true);

        if(yearField.isVisible() == true)yearField.setVisible(false);
        else yearField.setVisible(true);
    }

}
