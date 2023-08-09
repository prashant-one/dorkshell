package com.prashant.javashell.provider;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class DorkShellProvider implements PromptProvider {
    @Override
    public AttributedString getPrompt() {
        return new AttributedString("$dork-shell:>",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE)
        );
    }
}
