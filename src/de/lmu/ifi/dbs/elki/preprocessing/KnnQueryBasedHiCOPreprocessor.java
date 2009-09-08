package de.lmu.ifi.dbs.elki.preprocessing;

import java.util.ArrayList;
import java.util.List;

import de.lmu.ifi.dbs.elki.data.FeatureVector;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.DistanceResultPair;
import de.lmu.ifi.dbs.elki.distance.DoubleDistance;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.IntParameter;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.OptionID;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.ParameterException;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.constraints.GreaterConstraint;

/**
 * Computes the HiCO correlation dimension of objects of a certain database. The
 * PCA is based on k nearest neighbor queries.
 * 
 * @author Elke Achtert
 * @param <V> Vector type
 */
public class KnnQueryBasedHiCOPreprocessor<V extends FeatureVector<V, ? extends Number>> extends HiCOPreprocessor<V> {
  /**
   * OptionID for {@link #K_PARAM}
   */
  public static final OptionID KNN_HICO_PREPROCESSOR_K = OptionID.getOrCreateOptionID("hicopreprocessor.k", "The number of nearest neighbors considered in the PCA. " + "If this parameter is not set, k ist set to three " + "times of the dimensionality of the database objects.");

  /**
   * Optional parameter to specify the number of nearest neighbors considered in
   * the PCA, must be an integer greater than 0. If this parameter is not set, k
   * ist set to three times of the dimensionality of the database objects.
   * <p>
   * Key: {@code -hicopreprocessor.k}
   * </p>
   * <p>
   * Default value: three times of the dimensionality of the database objects
   * </p>
   */
  private final IntParameter K_PARAM = new IntParameter(KNN_HICO_PREPROCESSOR_K, new GreaterConstraint(0), true);

  /**
   * Holds the value of parameter k.
   */
  private Integer k;

  /**
   * Provides a new Preprocessor that computes the correlation dimension of
   * objects of a certain database based on a k nearest neighbor query.
   */
  public KnnQueryBasedHiCOPreprocessor() {
    super();
    addOption(K_PARAM);
  }

  @Override
  protected List<Integer> objectIDsForPCA(Integer id, Database<V> database, boolean verbose, boolean time) {
    if(k == null) {
      V obj = database.get(id);
      k = 3 * obj.getDimensionality();
    }

    pcaDistanceFunction.setDatabase(database, verbose, time);
    List<DistanceResultPair<DoubleDistance>> knns = database.kNNQueryForID(id, k, pcaDistanceFunction);

    List<Integer> ids = new ArrayList<Integer>(knns.size());
    for(DistanceResultPair<DoubleDistance> knn : knns) {
      ids.add(knn.getID());
    }

    return ids;
  }

  @Override
  protected List<DistanceResultPair<DoubleDistance>> resultsForPCA(Integer id, Database<V> database, boolean verbose, boolean time) {
    if(k == null) {
      V obj = database.get(id);
      k = 3 * obj.getDimensionality();
    }

    pcaDistanceFunction.setDatabase(database, verbose, time);
    return database.kNNQueryForID(id, k, pcaDistanceFunction);
  }

  /**
   * Sets the value for the parameter k. If k is not specified, the default
   * value is used.
   * 
   */
  @Override
  public List<String> setParameters(List<String> args) throws ParameterException {
    List<String> remainingParameters = super.setParameters(args);

    if(K_PARAM.isSet()) {
      k = K_PARAM.getValue();
    }

    rememberParametersExcept(args, remainingParameters);
    return remainingParameters;
  }

  @Override
  public String shortDescription() {
    StringBuffer description = new StringBuffer();
    description.append(KnnQueryBasedHiCOPreprocessor.class.getName());
    description.append(" computes the correlation dimension of objects of a certain database.\n");
    description.append("The PCA is based on k nearest neighbor queries.\n");
    return description.toString();
  }
}
