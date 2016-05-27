package org.games.mvc.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.games.core.PieceKind;

/**
 * Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>
 * <p>
 * Copyright 2016 by Rigoberto Leander Salgado Reyes.
 * <p>
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.
 */
class PieceView extends Label {
    PieceKind kind;

    PieceView(PieceKind kind, double size) {
        update(kind, size);
    }

    void update(PieceKind kind) {
        final ImageView  graphic = (ImageView) getGraphic();
        update(kind, graphic.getFitHeight());
    }

    private void update(PieceKind kind, double size) {
        this.kind = kind;
        String image;
        switch (kind) {
            case BLACK:
                image = "images/blackCell128x128.png";
                break;
            case WHITE:
                image = "images/whiteCell128x128.png";
                break;
            case NEUTRON:
                image = "images/neutronCell128x128.png";
                break;
            case SBLACK:
                image = "images/sBlackCell128x128.png";
                break;
            case SWHITE:
                image = "images/sWhiteCell128x128.png";
                break;
            case SNEUTRON:
                image = "images/sNeutronCell128x128.png";
                break;
            case SCELL:
                image = "images/sCell128x128.png";
                break;
            default:
                image = "images/cell128x128.png";
        }
        ImageView imageView = new ImageView(new Image(getClass().getClassLoader().getResource(image).toExternalForm()));
        imageView.setFitHeight(size);
        imageView.setFitWidth(size);
        imageView.setPreserveRatio(true);
        setGraphic(imageView);
    }
}
