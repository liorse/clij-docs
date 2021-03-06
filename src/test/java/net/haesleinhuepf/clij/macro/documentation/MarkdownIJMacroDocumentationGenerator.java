package net.haesleinhuepf.clij.macro.documentation;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij.macro.documentation.MarkDownDocumentationTemplate;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * MarkdownIJMacroDocumentationGenerator
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
public class MarkdownIJMacroDocumentationGenerator {
    public static void main(String... args) throws IOException {
        generateMethodReference();
    }

    private static void generateMethodReference() throws IOException {
        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);

        StringBuilder documentation = new StringBuilder();
        documentation.append("# CLIJ reference for ImageJ macro\n\n");

        ArrayList<String> methodNames = new ArrayList<String>();
        methodNames.addAll(service.getCLIJMethodNames());
        Collections.sort(methodNames);

        for (String name : methodNames) {
            CLIJMacroPlugin plugin = service.getCLIJMacroPlugin(name);
            documentation.append("<a name=\"" + name + "\"></a>\n");
            if (plugin instanceof OffersDocumentation) {
                OffersDocumentation documentedPlugin = (OffersDocumentation) plugin;
                documentation.append(new MarkDownDocumentationTemplate(documentedPlugin.getDescription(), documentedPlugin.getAvailableForDimensions(), plugin));
            } else {
                documentation.append("## " + name);
                documentation.append("Parameters: " + plugin.getParameterHelpText());
            }

            String linkToExamples = AbstractDocumentationGenerator.searchForExampleScripts(name, "src/main/macro/", "https://github.com/clij/clij-docs/blob/master/src/main/macro/");
            if(linkToExamples.length() > 0) {
                documentation.append("\n\n### Example scripts\n" + linkToExamples + "\n\n");
            }

        }
        documentation.append("\n\n" + methodNames.size() + " plugins documented.");

        documentation.append("[Back to CLIJ documentation](https://clij.github.io/)\n" +
                "\n" +
                "[Imprint](https://clij.github.io/imprint)\n");

        FileWriter writer = new FileWriter(new File("reference.md"));
        writer.write(documentation.toString());
        writer.close();
    }
}
