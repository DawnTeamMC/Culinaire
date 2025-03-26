package fr.hugman.culinaire.tea;

import fr.hugman.culinaire.component.TeaTypesComponent;

public class TeaHelper {
    public static int getColor(TeaTypesComponent teaTypes) {
        if (teaTypes.isEmpty()) {
            return 15112486;
        } else {
            float f = 0.0F;
            float g = 0.0F;
            float h = 0.0F;
            int j = 0;
            for (var teaType : teaTypes.getTeaTypeEntries()) {
                int k = teaType.getKey().value().color();
                int l = teaType.getIntValue() + 1;
                f += (float) (l * (k >> 16 & 255)) / 255.0F;
                g += (float) (l * (k >> 8 & 255)) / 255.0F;
                h += (float) (l * (k & 255)) / 255.0F;
                j += l;
            }
            if (j == 0) {
                return 0;
            } else {
                f = f / (float) j * 255.0F;
                g = g / (float) j * 255.0F;
                h = h / (float) j * 255.0F;
                return (int) f << 16 | (int) g << 8 | (int) h;
            }
        }
    }
}
