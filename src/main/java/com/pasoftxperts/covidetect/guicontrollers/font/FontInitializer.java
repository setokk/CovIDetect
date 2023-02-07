package com.pasoftxperts.covidetect.guicontrollers.font;

import com.pasoftxperts.covidetect.RunApplication;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

// Author: setokk
// LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
/**
 * This class is used for initializing the default app font.
 **/
public final class FontInitializer
{
    private static final Font font = Font
            .loadFont(RunApplication.class
            .getResourceAsStream("/com/pasoftxperts/covidetect/fonts/Corbel_Light.TTF"), 6.0);

    /**
     * Initializes the font to Corbel Light of all nodes contained in a parent.
     * @param parent: the parent root of the scene
    **/
    public static void initializeFont(Parent parent)
    {
        var sceneChildren = getAllNodes(parent);
        for (var element : sceneChildren)
        {
            if (element instanceof Labeled label)
            {
                double size = label.getFont().getSize();
                label.setFont(Font.font("Corbel Light", FontWeight.NORMAL, size));
            }
            else if (element instanceof TextField textField)
            {
                double size = textField.getFont().getSize();
                textField.setFont(Font.font("Corbel Light", FontWeight.NORMAL, size));
            }
        }
    }

    private static ArrayList<Node> getAllNodes(Parent root)
    {
        ArrayList<Node> nodes = new ArrayList<>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes)
    {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }

}
