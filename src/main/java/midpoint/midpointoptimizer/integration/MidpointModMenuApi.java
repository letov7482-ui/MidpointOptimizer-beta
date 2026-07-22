package midpoint.midpointoptimizer.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import midpoint.midpointoptimizer.config.screen.MidpointConfigScreen;

public class MidpointModMenuApi implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new MidpointConfigScreen(parent);
    }
}
