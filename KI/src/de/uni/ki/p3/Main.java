package de.uni.ki.p3;

import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import de.uni.ki.p3.Drawing.MapObject;
import de.uni.ki.p3.SVG.SVGParsing;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class Main extends Application{
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("KIsches Wunder");
        Group root = new Group();
        
        //
        SVGDocument svgDoc = GetSVGDocument();
        MapObject mapObject = new MapObject();
        mapObject.parseSVGDocument(svgDoc);
        Canvas canvas = new Canvas(GetSVGWidth(svgDoc), GetSVGHeight(svgDoc));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        MapObject.drawMapObject(gc, mapObject);
        //
        
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    public SVGDocument GetSVGDocument()
    {
    	String filePath = "C:\\Dev\\Git\\Uni\\KI\\KI\\img\\street.svg";
    	return SVGParsing.toSVGDocument(filePath);
    	
    }
    
    public static double GetSVGHeight(SVGDocument svgDocument)
    {
    	NodeList nl = svgDocument.getElementsByTagName("svg");
    	String value = nl.item(0).getAttributes().getNamedItem("height").getNodeValue();
    	return Double.parseDouble(value);
    }
    
    public static double GetSVGWidth(SVGDocument svgDocument)
    {
    	NodeList nl = svgDocument.getElementsByTagName("svg");
    	String value = nl.item(0).getAttributes().getNamedItem("width").getNodeValue();
    	return Double.parseDouble(value);
    }

    
}