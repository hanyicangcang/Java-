package de.lmu.ifi.dbs.elki.wrapper;

import de.lmu.ifi.dbs.elki.utilities.optionhandling.FileParameter;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.ParameterException;

import java.io.File;

/**
 * StandAloneInputWrapper extends StandAloneWrapper and
 * sets additionally the parameter in. <p/> Any
 * Wrapper class that makes use of these flags may extend this class. Beware to
 * make correct use of parameter settings via optionHandler as commented with
 * constructor and methods.
 *
 * @author Elke Achtert
 *         todo parameter
 */
public abstract class StandAloneInputWrapper extends StandAloneWrapper {

    /**
     * Label for parameter input.
     */
    public final static String INPUT_P = "in";

    /**
     * Description for parameter input.
     */
    public static String INPUT_D = "input file";

    /**
     * The input file.
     */
    private File input;

    /**
     * Adds parameter
     * {@link } todo
     * to the option handler additionally to parameters of super class.
     */
    protected StandAloneInputWrapper() {
        super();
        optionHandler.put(new FileParameter(INPUT_P, INPUT_D,
            FileParameter.FileType.INPUT_FILE));
    }

    /**
     * @see de.lmu.ifi.dbs.elki.utilities.optionhandling.Parameterizable#setParameters(String[])
     */
    public String[] setParameters(String[] args) throws ParameterException {
        String[] remainingParameters = super.setParameters(args);
        // input
        input = (File) optionHandler.getOptionValue(INPUT_P);
        return remainingParameters;
    }

    /**
     * Returns the input string.
     *
     * @return the input string
     */
    public final File getInput() {
        return input;
    }
}
