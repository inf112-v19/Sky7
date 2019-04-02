package sky7.board.cellContents.Active;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.IActive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Supplier;

public class Belt implements IActive {
    private DIRECTION directionFromAlt = null;
    private DIRECTION directionTo = null;
    private DIRECTION directionFrom = null;
    private int type;//1(blue belt) is double(two steps), 0(yellow belt) is simple(one step)
    private Texture texture;
    private static final int PRIORITY = 4;

    public Belt(DIRECTION directionFrom, DIRECTION directionTo, int type) {
        this.directionFrom = directionFrom;
        this.directionTo = directionTo;
        this.type = type;
    }

    public Belt(DIRECTION directionFrom, DIRECTION directionFromAlt, DIRECTION directionTo, int type) {
        this(directionFrom, directionTo, type);
        this.directionFromAlt = directionFromAlt;
        this.type = type;
    }

    @Override
    public Texture getTexture() {
        String path = "";
        if (texture == null) {
            switch (type) {
                case 0:
                    if (directionFromAlt != null)
                        path = "assets/floor/Conv/blue/ConvB_" + directionFrom.symbol() + directionFromAlt.symbol() + directionTo.symbol() + ".png";
                    else
                        path = "assets/floor/Conv/blue/ConvB_" + directionFrom.symbol() + directionTo.symbol() + ".png";
                    break;
                case 1:
                    if (directionFromAlt != null)
                        path = "assets/floor/Conv/yellow/Conv" + directionFrom.symbol() + directionFromAlt.symbol() + directionTo.symbol() + ".png";
                    else
                        path = "assets/floor/Conv/yellow/Conv" + directionFrom.symbol() + directionTo.symbol() + ".png";
                    break;
            }
            texture = new Texture(path);
        }
        return texture; //TODO: add Belt Texture
    }

    @Override
    public int drawPriority() {
        return PRIORITY;
    }

    @Override
    public int compareTo(ICell other) {
        return Integer.compare(this.drawPriority(), other.drawPriority());
    }

    public static List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> getSuppliers() {
        List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> suppliers = new ArrayList<>();
        final char[] dirSym = {'N', 'S', 'E', 'W'};
        for (int i = 0; i < dirSym.length; i++) {
            final int a = i;
            for (int j = 0; j < dirSym.length; j++) {
                if (i != j) {
                    final int b = j;
                    suppliers.add(
                            new AbstractMap.SimpleEntry<>(
                                    "b" + dirSym[i] + dirSym[j] + 0,
                                    () -> new Belt(
                                            DIRECTION.values()[a],
                                            DIRECTION.values()[b],
                                            0
                                    )));
                    suppliers.add(
                            new AbstractMap.SimpleEntry<>(
                                    "b" + dirSym[i] + dirSym[j] + 1,
                                    () -> new Belt(
                                            DIRECTION.values()[a],
                                            DIRECTION.values()[b],
                                            1
                                    )));
                }
            }
        }

        for (int i = 0; i < dirSym.length; i++) {
            final int a = i;
            for (int j = i; j < dirSym.length; j++) {
                final int b = j;
                for (int k = 0; k < dirSym.length; k++) {
                    if (i != j && k != i && k != j) {
                        final int c = k;
                        suppliers.add(
                                new AbstractMap.SimpleEntry<>(
                                        "b" + dirSym[i] + dirSym[j] + dirSym[k] + 0,
                                        () -> new Belt(
                                                DIRECTION.values()[a],
                                                DIRECTION.values()[b],
                                                DIRECTION.values()[c],
                                                0)));
                        suppliers.add(
                                new AbstractMap.SimpleEntry<>(
                                        "b" + dirSym[i] + dirSym[j] + dirSym[k] + 1,
                                        () -> new Belt(
                                                DIRECTION.values()[a],
                                                DIRECTION.values()[b],
                                                DIRECTION.values()[c],
                                                1)));
                    }
                }
            }
        }
        for (int i = 0; i < suppliers.size(); i++) {
            System.out.println(suppliers.get(i).getKey());
        }
        return suppliers;
    }
}
