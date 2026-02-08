package com.cristiangomez.tema4maven;

import com.github.lalyos.jfiglet.FigletFont;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        //Variables
        final String NAME = "Cristian Gomez";
        String banner = FigletFont.convertOneLine(NAME);
        final int VELOCIDAD_ANIMACION = 100;

        List<String> guion = new ArrayList<>(); //Creo la lista (rollo de película)

        //Añado el banner (esto trocea el banner para que entre línea por línea)
        for (String linea : banner.split("\n")) {
            guion.add(linea);
        }

        //Añado mi CV a la lista
        guion.addAll(Arrays.asList(
                "",
                "=== CURRICULUM VITAE ===",
                "",
                "Puesto actual:",
                "   Estudiante de 1er curso de DAW",
                "   (Desarrollo de Aplicaciones Web)",
                "",
                "Tecnologías y Habilidades:",
                "--------------------------",
                "* Lenguajes de Marca: HTML & CSS",
                "* SQL",
                "* Java",
                "* Control de versiones: Git & GitHub",
                "* Administración de sistemas: Bash",
                "* Herramientas de construcción: Maven",
                "",
                "¡Gracias por ver mi presentación!"
        ));

       //Inicializar la pantalla con Laterna
        Screen screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        screen.setCursorPosition(null); // Oculta el cursor

        //Animación (Bucle principal)

        //  Si yOffset es grande (por ejemplo height), el contenido empieza “debajo” y entra.
        int yOffset = screen.getTerminalSize().getRows();

        // Bucle infinito o hasta que se acabe el texto
        // Condicion de salida para que no sea infinito
        while (true) {

            //Dibujar el texto
            drawFrame(screen, guion, yOffset);

            try {
                Thread.sleep(VELOCIDAD_ANIMACION); // Pausa para controlar la velocidad de la animación
            } catch (InterruptedException ignored) { }

            // Cambiar el offset (mover hacia arriba)
            yOffset--;

            // COMPROBACIÓN: Si el texto ya ha subido del todo (ha desaparecido por arriba)
            // -guion.size() es la posición donde la última línea justo sale de la pantalla
            if (yOffset < -guion.size()) {
                // Reinicio la posición al valor original (altura de la ventana)
                yOffset = screen.getTerminalSize().getRows();
            }
        }

    }

    // Este método se encarga de dibujar un "fotograma" de la animación
    private static void drawFrame(Screen screen, List<String> guion, int yOffset) throws IOException {
        TerminalSize size = screen.getTerminalSize();
        int width = size.getColumns();
        int height = size.getRows();

        screen.clear(); // Limpiamos la pantalla antes de dibujar
        TextGraphics tg = screen.newTextGraphics();

        for (int i = 0; i < guion.size(); i++) {
            // Calculamos la posición vertical
            int y = yOffset + i;

            // Si la línea se sale de la pantalla por arriba o abajo, no la pintamos
            if (y < 0 || y >= height) continue;

            String line = guion.get(i);

            // Centrado horizontal (calculamos la X para que quede en el medio)
            int x = Math.max(0, (width - line.length()) / 2);

            // Recorte simple si la línea es más ancha que la pantalla
            String visible = (line.length() > width) ? line.substring(0, width) : line;

            // Pintamos la línea
            tg.putString(x, y, visible);
        }
        screen.refresh(); // Mostramos los cambios
    }
}
