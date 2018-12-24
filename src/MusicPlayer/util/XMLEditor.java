/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer.util;

import MusicPlayer.model.Library;
import MusicPlayer.model.Song;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author elezo
 */
public class XMLEditor {
    private static String musicDirectoryPath;
    
    public static String UNKNOWN_SONG = "Unknown Song";
    public static String UNKNOWN_ARTIST = "Unknown Artist";
    public static String UNKNOWN_ALBUM = "Unknown Album";
    
    public static int lastIdAssigned = 0;
    
    
    private static ArrayList<Song> songsToAdd = new ArrayList<>();
    private static ArrayList<File> songFilesToAdd = new ArrayList();
    private static ArrayList<String> xmlSongFileNames = new ArrayList<>();
    
    public static ObservableList<Song> getObservableSongsToAdd() {
        return FXCollections.observableArrayList(songsToAdd);
    }
    
    public static ArrayList<Song> getSongsToAdd(){
        return songsToAdd;
    }
    
    public static ArrayList<String> getXmlFileNames(){
        return xmlSongFileNames;
    }
    
    public static ArrayList<File> getSongFilesToAdd(){
        return songFilesToAdd;
    }
    
    public static void clearSongsToAdd(){
        songsToAdd.clear();
    }
    
    public static void setFilesToAdd(List<File> fileList){
        songFilesToAdd = (ArrayList<File>) fileList;
    }
    
    public static void addOnStartup(){
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("src/resources/library.xml");
            
            NodeList nodeList = doc.getElementsByTagName("song");
            for(int i=0; i < nodeList.getLength(); i++){
                Node songNode = nodeList.item(i);
                if(songNode.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) songNode;
                    String songPath = getTagValue("location", element);
                    xmlSongFileNames.add(songPath);   
                }
            }
        
            
        }   
            catch (Exception ex){
                    ex.printStackTrace();
            }
    }
    
    private static String getTagValue(String tag, Element element){
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node value = (Node) nodeList.item(0);
        return value.getNodeValue();
    }
    
    public static void addSongToXml(){
        //Fills songsToAddArray; 
        createSongObject();
        
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("src/resources/library.xml");
            
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("/library/songs");
            Node songsNode = ((NodeList) expr.evaluate(doc, XPathConstants.NODESET)).item(0);
            
            for (Song song : songsToAdd) {
                //add song to master library
                //Library.addSongToLibrary(song);
                //Library.addArtistToLibrary();
                //Library.addAlbumToLibrary();
                
                Element newSong = doc.createElement("song");
                Element newSongId = doc.createElement("id");
                Element newSongTitle = doc.createElement("title");
                Element newSongArtist = doc.createElement("artist");
                Element newSongAlbum = doc.createElement("album");
                Element newSongLength = doc.createElement("length");
                Element newSongTrackNumber = doc.createElement("trackNumber");
                Element newSongDiscNumber = doc.createElement("discNumber");
                Element newSongPlayCount = doc.createElement("playCount");
                Element newSongLocation = doc.createElement("location");
            
                newSongId.setTextContent(Integer.toString(song.getId()));
                newSongTitle.setTextContent(song.getTitle());
                newSongArtist.setTextContent(song.getArtist());
                newSongAlbum.setTextContent(song.getAlbum());
                newSongLength.setTextContent(song.getLength());
                newSongPlayCount.setTextContent(Integer.toString(song.getPlayCount()));
                newSongLocation.setTextContent(song.getLocation());
                
                songsNode.appendChild(newSong);
                newSong.appendChild(newSongId);
                newSong.appendChild(newSongTitle);
                newSong.appendChild(newSongArtist);
                newSong.appendChild(newSongAlbum);
                newSong.appendChild(newSongLength);
                newSong.appendChild(newSongTrackNumber);
                newSong.appendChild(newSongDiscNumber);
                newSong.appendChild(newSongPlayCount);
                newSong.appendChild(newSongLocation);
                
            
        }
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            File xmlFile = new File("src/resources/library.xml");
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
                
        }
            
        
        catch (Exception ex) {
			ex.printStackTrace();
	}
        
    }
    
    public static void createSongObject() {
        
        
        for(File songFile : songFilesToAdd){
        try{
        AudioFile audioFile = AudioFileIO.read(songFile);
        Tag tag = audioFile.getTag();
        AudioHeader header = audioFile.getAudioHeader();
        
        String title = tag.getFirst(FieldKey.TITLE);
                   
        String artistTitle = tag.getFirst(FieldKey.ALBUM_ARTIST);
        if (artistTitle.equals(null) || artistTitle.equals("") || artistTitle.equals("null")){
            artistTitle = tag.getFirst(FieldKey.ARTIST);
        }
        if (artistTitle.equals(null) || artistTitle.equals("") || artistTitle.equals("null")){
            artistTitle = UNKNOWN_ARTIST;
        }
                
        String artist = (artistTitle == null || artistTitle.equals("") || artistTitle.equals("null")) ? "" : artistTitle;
        
        String album = tag.getFirst(FieldKey.ALBUM);
        if (album.equals(null) || album.equals("") || album.equals("null")){
            album = UNKNOWN_ALBUM;
        }

        Duration length = Duration.ofSeconds(header.getTrackLength());
        long lengthSeconds = length.getSeconds();
        long lengthMinutes = length.toMinutes();
        long seconds = length.getSeconds() % 60;
        String songLength = (length.toMinutes() + ":" + (seconds < 10 ? "0" + seconds : seconds));
        
        String track = tag.getFirst(FieldKey.TRACK);
        String disc = tag.getFirst(FieldKey.DISC_NO);
        
        int playCount = 0;
        String location = Paths.get(songFile.getAbsolutePath()).toString();
        
        int id = lastIdAssigned;
        lastIdAssigned++;
        
        Song songToAdd = new Song(title, artist, album, songLength, location, id, playCount);
        songsToAdd.add(songToAdd);
        Library.addSongToLibrary(songToAdd);
        }        
            catch (Exception ex){
                ex.printStackTrace();
            }
        
        
        }
    }
}
