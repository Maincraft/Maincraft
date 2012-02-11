package tk.maincraft.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import tk.maincraft.Maincraft;


public class TextWrap {
    protected TextWrap() {
        throw new UnsupportedOperationException();
    }

    private static final char COLOR_CHAR = '\u00A7';
    private static final char SPACE_CHAR = ' ';

    public static List<String> wrapText(String text) {
        String allowedChars = Maincraft.getServer().getConfig().getChatSettings().getAllowedChars();
        int chatStringLength = Maincraft.getServer().getConfig().getChatSettings()
                .getChatWrapLength();
        StringBuilder betterText = new StringBuilder();
        char[] allChars = text.toCharArray();
        for (int i = 0; i < allChars.length; i++) {
            if ((allowedChars.indexOf(allChars[i]) != -1) || (allChars[i] == COLOR_CHAR)
                    || (allChars[i - 1] == COLOR_CHAR)) {
                betterText.append(allChars[i]);
            }
        }
        text = betterText.toString();

        if (text.length() < chatStringLength) {
            List<String> ret = new ArrayList<String>(1);
            ret.add(text);
            return ret;
        }

        // else: text is too long :(
        List<String> ret = new ArrayList<String>();
        while (!text.isEmpty()) {
            if (text.length() <= chatStringLength) {
                ret.add(text);
                break;
            }

            int posSpace = text.lastIndexOf(SPACE_CHAR, chatStringLength);

            if (posSpace == -1) {
                // I can't help...
                // we have to cut the text :(
                posSpace = chatStringLength;
            }

            // posSpace is now the index of the last space in the first line. let's go ahead
            // and search the last color-char:
            int posColor = text.lastIndexOf(COLOR_CHAR);

            char colorChar = text.charAt(posColor + 1);
            char[] chars = text.toCharArray();
            StringBuilder respBuilder = new StringBuilder();
            for (int i = 0; i < posSpace; i++) {
                respBuilder.append(chars[i]);
            }
            String firstLine = respBuilder.toString();
            text = text.replaceFirst(Pattern.quote(firstLine + SPACE_CHAR), "");
            ret.add(firstLine);
            // handle color:
            if (posColor != -1) {
                String colorString = String.valueOf(COLOR_CHAR) + colorChar;
                text = colorString + text;
            }
        }

        return ret;
    }
}
