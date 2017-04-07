/*

    CS213
    Project 1: SongLibrary
    Michael Triano & Joseph Wolak

 */

package model;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
    -------------------------

    Class: SongCollection

    Contains the ArrayList of Songs and methods used to manipulate the data

    -------------------------
*/


public class SongCollection {

    public static ArrayList<Song> listOfSongs;

    public SongCollection() {
        listOfSongs = new ArrayList<Song>();
        importFromFile();
    }

    /*
        -------------------------

        Class: Song

        Object that holds each individual Song's data

        -------------------------
    */

    public static class Song {

        String title, artist, year, album, key;

        public Song(String title, String artist, String year, String album) {
            this.title = title;
            this.artist = artist;
            this.year = year;
            this.album = album;
            this.key = title + artist;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
            this.key = title + artist;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
            this.key = title + artist;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }

    public static void sortList() {
        Collections.sort(listOfSongs, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());

            }
        });
    }

    public static void exportToFile() {
        try {
            PrintWriter printWriter = new PrintWriter("songlibrarydata.txt");
            printWriter.println("<begin>");
            for(int i = 0; i < listOfSongs.size(); i++) {
                printWriter.println("<song>");
                printWriter.println(listOfSongs.get(i).getTitle());
                printWriter.println(listOfSongs.get(i).getArtist());
                printWriter.println(listOfSongs.get(i).getYear());
                printWriter.println(listOfSongs.get(i).getAlbum());
            }
            printWriter.println("<end>");
            printWriter.close();
        } catch(FileNotFoundException f) {
            f.printStackTrace();
        }

    }

    private void importFromFile() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("songlibrarydata.txt"));
            String line = bufferedReader.readLine();
            String[] songData = new String[4];
            if(line.compareTo("<begin>") == 0) {
                while(true) {
                    line = bufferedReader.readLine();
                    if(line.compareTo("<song>") == 0) {
                        for(int i = 0; i < 4; i++) {
                            songData[i] = bufferedReader.readLine();
                        }
                        listOfSongs.add(new Song(songData[0], songData[1], songData[2], songData[3]));
                    } else return;
                }
            }
        } catch(FileNotFoundException f) {
            return;
        } catch(IOException i) {
            i.printStackTrace();
            return;
        }
    }
}
