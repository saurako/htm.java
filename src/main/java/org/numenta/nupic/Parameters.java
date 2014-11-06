/* ---------------------------------------------------------------------
 * Numenta Platform for Intelligent Computing (NuPIC)
 * Copyright (C) 2014, Numenta, Inc.  Unless you have an agreement
 * with Numenta, Inc., for a separate license for this software code, the
 * following terms and conditions apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *
 * http://numenta.org/licenses/
 * ---------------------------------------------------------------------
 */
package org.numenta.nupic;

import org.numenta.nupic.model.Cell;
import org.numenta.nupic.model.Column;
import org.numenta.nupic.research.ComputeCycle;
import org.numenta.nupic.research.SpatialPooler;
import org.numenta.nupic.research.TemporalMemory;
import org.numenta.nupic.util.BeanUtil;
import org.numenta.nupic.util.MersenneTwister;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Specifies parameters to be used as a configuration for a given {@link TemporalMemory}
 * or {@link SpatialPooler}
 *
 * @author David Ray
 * @see SpatialPooler
 * @see TemporalMemory
 * @see Connections
 * @see ComputeCycle
 */
@SuppressWarnings("unused")
public class Parameters {
    private static final Map<KEY, Object> DEFAULTS;

    static {
       Map<KEY, Object> defaultParams = new ParametersMap();

       /////////// Universal Parameters ///////////
       defaultParams.put(KEY.COLUMN_DIMENSIONS, new int[]{2048});
       defaultParams.put(KEY.CELLS_PER_COLUMN, 32);
       defaultParams.put(KEY.SEED, 42);
       defaultParams.put(KEY.RANDOM, new MersenneTwister((int)defaultParams.get(KEY.SEED)));

       /////////// Temporal Memory Parameters ///////////
       defaultParams.put(KEY.ACTIVATION_THRESHOLD, 13);
       defaultParams.put(KEY.LEARNING_RADIUS, 2048);
       defaultParams.put(KEY.MIN_THRESHOLD, 10);
       defaultParams.put(KEY.MAX_NEW_SYNAPSE_COUNT, 20);
       defaultParams.put(KEY.INITIAL_PERMANENCE, 0.21);
       defaultParams.put(KEY.CONNECTED_PERMANENCE, 0.5);
       defaultParams.put(KEY.PERMANENCE_INCREMENT, 0.10);
       defaultParams.put(KEY.PERMANENCE_DECREMENT, 0.10);
       defaultParams.put(KEY.TM_VERBOSITY, 0);

       /// Spatial Pooler Parameters ///////////
       defaultParams.put(KEY.INPUT_DIMENSIONS, new int[]{64});
       defaultParams.put(KEY.POTENTIAL_RADIUS, 16);
       defaultParams.put(KEY.POTENTIAL_PCT, 0.5);
       defaultParams.put(KEY.GLOBAL_INHIBITIONS, false);
       defaultParams.put(KEY.INHIBITION_RADIUS, 0);
       defaultParams.put(KEY.LOCAL_AREA_DENSITY, -1.0);
       defaultParams.put(KEY.NUM_ACTIVE_COLUMNS_PER_INH_AREA, 10);
       defaultParams.put(KEY.STIMULUS_THRESHOLD, 0.0);
       defaultParams.put(KEY.SYN_PERM_INACTIVE_DEC, 0.01);
       defaultParams.put(KEY.SYN_PERM_ACTIVE_INC, 0.1);
       defaultParams.put(KEY.SYN_PERM_CONNECTED, 0.10);
       defaultParams.put(KEY.SYN_PERM_BELOW_STIMULUS_INC, 0.01);
       defaultParams.put(KEY.SYN_PERM_TRIM_THRESHOLD, 0.5);
       defaultParams.put(KEY.MIN_PCT_OVERLAP_DUTY_CYCLE, 0.001);
       defaultParams.put(KEY.MIN_PCT_ACTIVE_DUTY_CYCLE, 0.001);
       defaultParams.put(KEY.DUTY_CYCLE_PERIOD, 1000);
       defaultParams.put(KEY.MAX_BOOST, 10.0);
       defaultParams.put(KEY.SP_VERBOSITY, 0);

       //////////// Encoder Parameters ///////////
       defaultParams.put(KEY.W, 0);
       defaultParams.put(KEY.MINVAL, .0);
       defaultParams.put(KEY.MAXVAL, .0);
       defaultParams.put(KEY.PERIODIC, false);
       defaultParams.put(KEY.N, 0);
       defaultParams.put(KEY.RADIUS, .0);
       defaultParams.put(KEY.RESOLUTION, .0);
       defaultParams.put(KEY.NAME, "None");
       defaultParams.put(KEY.CLIP_INPUT, false);
       defaultParams.put(KEY.FORCED, false);
       defaultParams.put(KEY.ENC_VERBOSITY, 0);
       DEFAULTS = Collections.unmodifiableMap(defaultParams);
    }

    /**
     * Constant values representing configuration parameters for the {@link TemporalMemory}
     */
    public static enum KEY {
        /////////// Universal Parameters ///////////
        COLUMN_DIMENSIONS("columnDimensions", int[].class),
        CELLS_PER_COLUMN("cellsPerColumn", Integer.class),
        RANDOM("random", Random.class),
        SEED("seed", Integer.class),

        /////////// Temporal Memory Parameters ///////////
        ACTIVATION_THRESHOLD("activationThreshold", Integer.class),
        LEARNING_RADIUS("learningRadius", Integer.class),
        MIN_THRESHOLD("minThreshold", Integer.class),
        MAX_NEW_SYNAPSE_COUNT("maxNewSynapseCount", Integer.class),
        INITIAL_PERMANENCE("initialPermanence", Double.class),
        CONNECTED_PERMANENCE("connectedPermanence", Double.class),
        PERMANENCE_INCREMENT("permanenceIncrement", Double.class),
        PERMANENCE_DECREMENT("permanenceDecrement", Double.class),
        TM_VERBOSITY("tmVerbosity", Integer.class),

        /////////// Spatial Pooler Parameters ///////////
        INPUT_DIMENSIONS("inputDimensions", int[].class),
        POTENTIAL_RADIUS("potentialRadius", Integer.class),
        POTENTIAL_PCT("potentialPct", Double.class),
        GLOBAL_INHIBITIONS("globalInhibition", Boolean.class),
        INHIBITION_RADIUS("inhibitionRadius", Integer.class),
        LOCAL_AREA_DENSITY("localAreaDensity", Double.class),
        NUM_ACTIVE_COLUMNS_PER_INH_AREA("numActiveColumnsPerInhArea", Integer.class),
        STIMULUS_THRESHOLD("stimulusThreshold", Double.class),
        SYN_PERM_INACTIVE_DEC("synPermInactiveDec", Double.class),
        SYN_PERM_ACTIVE_INC("synPermActiveInc", Double.class),
        SYN_PERM_CONNECTED("synPermConnected", Double.class),
        SYN_PERM_BELOW_STIMULUS_INC("synPermBelowStimulusInc", Double.class),
        SYN_PERM_TRIM_THRESHOLD("synPermTrimThreshold", Double.class),
        MIN_PCT_OVERLAP_DUTY_CYCLE("minPctOverlapDutyCycles", Double.class),
        MIN_PCT_ACTIVE_DUTY_CYCLE("minPctActiveDutyCycles", Double.class),
        DUTY_CYCLE_PERIOD("dutyCyclePeriod", Integer.class),
        MAX_BOOST("maxBoost", Double.class),
        SP_VERBOSITY("spVerbosity", Integer.class),

        //////////// Encoder Parameters ///////////
        W("w", Integer.class),
        MINVAL("minval", Double.class),
        MAXVAL("maxval", Double.class),
        PERIODIC("periodic", Boolean.class),
        N("n", Integer.class),
        RADIUS("radius", Double.class),
        RESOLUTION("resolution", Double.class),
        NAME("name", String.class),
        CLIP_INPUT("clipInput", Boolean.class),
        FORCED("forced", Boolean.class),
        ENC_VERBOSITY("encVerbosity", Integer.class),

        //////////// Category Encoder Parameters /////////////
        CATEGORY_LIST("categoryList", List.class);

        final private String fieldName;
        final private Class<?> fieldType;
        //TODO add range automatic range check for parameters, same as type

        /**
         * Constructs a new KEY
         *
         * @param fieldName
         * @param fieldType
         */
        private KEY(String fieldName, Class<?> fieldType) {
            this.fieldName = fieldName;
            this.fieldType = fieldType;
        }

        public Class<?> getFieldType() {
            return fieldType;
        }

        public String getFieldName() {
            return fieldName;
        }
    }

    /**
     * Save guard decorator around params map
     */
    private static class ParametersMap extends EnumMap<KEY, Object> {
        ParametersMap() {
            super(Parameters.KEY.class);
        }

        @Override public Object put(KEY key, Object value) {
            if (!key.getFieldType().isInstance(value)) {
                throw new IllegalArgumentException(
                        "Can not set Parameters Property '" + key.getFieldName() + "' because of type mismatch. The required type is " + key.getFieldType());
            }
            return super.put(key, value);
        }
    }

    /**
    * Map of parameters to their values
    */
    private EnumMap<Parameters.KEY, Object> paramMap = new ParametersMap();




    /**
     * Constructs a new {@code Parameters} object.
     */
    public Parameters() {
    }

    /**
     * @deprecated  This constructor has 27 parameters, many of them same type, so it is error prone
     * Sets up default parameters for both the {@link SpatialPooler} and the
     * {@link TemporalMemory}
     *
     * @param inputDimensions
     * @param columnDimensions
     * @param cellsPerColumn
     * @param potentialRadius
     * @param potentialPct
     * @param globalInhibition
     * @param localAreaDensity
     * @param numActiveColumnsPerInhArea
     * @param stimulusThreshold
     * @param synPermInactiveDec
     * @param synPermActiveInc
     * @param synPermConnected
     * @param synPermBelowStimulusInc
     * @param minPctOverlapDutyCycles
     * @param minPctActiveDutyCycles
     * @param dutyCyclePeriod
     * @param maxBoost
     * @param activationThreshold
     * @param learningRadius
     * @param minThreshold
     * @param maxNewSynapseCount
     * @param seed
     * @param initialPermanence
     * @param connectedPermanence
     * @param permanenceIncrement
     * @param permanenceDecrement
     * @param random
     */
    @Deprecated
    public Parameters(int[] inputDimensions, int[] columnDimensions, int cellsPerColumn,
                      int potentialRadius/*SP*/, double potentialPct/*SP*/, boolean globalInhibition/*SP*/,
                      double localAreaDensity/*SP*/, double numActiveColumnsPerInhArea/*SP*/, double stimulusThreshold/*SP*/,
                      double synPermInactiveDec/*SP*/, double synPermActiveInc/*SP*/, double synPermConnected/*SP*/,
                      double synPermBelowStimulusInc/*SP*/, double minPctOverlapDutyCycles/*SP*/,
                      double minPctActiveDutyCycles/*SP*/,
                      int dutyCyclePeriod/*SP*/, double maxBoost/*SP*/, int activationThreshold/*TM*/,
                      int learningRadius/*TM*/, int minThreshold/*TM*/,
                      int maxNewSynapseCount/*TM*/, int seed, double initialPermanence/*TM*/,
                      double connectedPermanence/*TM*/, double permanenceIncrement/*TM*/,
                      double permanenceDecrement/*TM*/, int w/*ENC*/, int n/*ENC*/, int radius/*ENC*/,
                      int resolution/*ENC*/, boolean periodic/*ENC*/, boolean clipInput/*ENC*/, boolean forced/*ENC*/,
                      int minval/*ENC*/, int maxval/*ENC*/, String name/*ENC*/, Random random) {

        super();

        //SpatialPooler
        setInputDimensions(inputDimensions);
        setColumnDimensions(columnDimensions);
        setCellsPerColumn(cellsPerColumn);
        setPotentialRadius(potentialRadius);
        setPotentialPct(potentialPct);
        setGlobalInhibition(globalInhibition);
        setLocalAreaDensity(localAreaDensity);
        setNumActiveColumnsPerInhArea(numActiveColumnsPerInhArea);
        setStimulusThreshold(stimulusThreshold);
        setSynPermInactiveDec(synPermInactiveDec);
        setSynPermActiveInc(synPermActiveInc);
        setSynPermConnected(synPermConnected);
        setSynPermBelowStimulusInc(synPermBelowStimulusInc);
        setMinPctOverlapDutyCycle(minPctOverlapDutyCycles);
        setMinPctActiveDutyCycle(minPctActiveDutyCycles);
        setDutyCyclePeriod(dutyCyclePeriod);
        setMaxBoost(maxBoost);

        //TemporalMemory
        setActivationThreshold(activationThreshold);
        setLearningRadius(learningRadius);
        setMinThreshold(minThreshold);
        setMaxNewSynapseCount(maxNewSynapseCount);
        setSeed(seed);
        setInitialPermanence(initialPermanence);
        setConnectedPermanence(connectedPermanence);
        setPermanenceIncrement(permanenceIncrement);
        setPermanenceDecrement(permanenceDecrement);
        setRandom(random);
    }

    /**
     * Copy constructor for convenience
     *
     * @param other
     */
    public Parameters(Parameters other) {
        for(KEY key: KEY.values()){
            this.setParameterByKey(key, other.getParameterByKey(key));
        }
    }

    /**
     * @deprecated  Will have private access, replaced by {@link #setParameterByKey(KEY, Object)}. Need it to to be compatible with EnumMap<Parameters.KEY, Object> p = parameters.getMap() in tests.
     * Creates and returns an {@link EnumMap} containing the specified keys; whose
     * values are to be loaded later. The map returned is only created if the internal
     * reference is null (never been created), therefore the map is a singleton which
     * cannot be recreated once created.
     *
     * @return
     */
    @Deprecated
    public EnumMap<Parameters.KEY, Object> getMap() {
        return paramMap;
    }

    /**
    * Set parameter by Key{@link KEY}
    * @param key
    * @param value
    */
    public void setParameterByKey(KEY key, Object value){
      paramMap.put(key, value);
    }

    /**
     * Get parameter by Key{@link KEY}
     * @param key
     * @return
     */
    public Object getParameterByKey(KEY key){
        return paramMap.get(key);
    }

    public Set<KEY> getKeysForPresent(){
        return paramMap.keySet();
    }

    /**
     * Sets the fields specified by the {@code Parameters} on the specified
     * {@link Connections} object.
     *
     * @param cn
     * @param p
     */
    public static void apply(Object cn, Parameters p) {
        BeanUtil beanUtil = BeanUtil.getInstance();
        for(KEY key: p.getKeysForPresent()){
            beanUtil.setSimpleProperty(cn, key.fieldName, p.getParameterByKey(key));
        }
    }

    //TODO I'm not sure we need maintain implicit setters below. Kinda contradict unified access with KEYs

    /**
     * Returns the seeded random number generator.
     *
     * @param r the generator to use.
     */
    public void setRandom(Random r) {
        paramMap.put(KEY.RANDOM, r);
    }

    /**
     * Sets the number of {@link Column}.
     *
     * @param columnDimensions
     */
    public void setColumnDimensions(int[] columnDimensions) {
        paramMap.put(KEY.COLUMN_DIMENSIONS, columnDimensions);
    }

    /**
     * Sets the number of {@link Cell}s per {@link Column}
     *
     * @param cellsPerColumn
     */
    public void setCellsPerColumn(int cellsPerColumn) {
        paramMap.put(KEY.CELLS_PER_COLUMN, cellsPerColumn);
    }

    /**
     * Sets the activation threshold.
     * <p/>
     * If the number of active connected synapses on a segment
     * is at least this threshold, the segment is said to be active.
     *
     * @param activationThreshold
     */
    public void setActivationThreshold(int activationThreshold) {
        paramMap.put(KEY.ACTIVATION_THRESHOLD, activationThreshold);
    }

    /**
     * Radius around cell from which it can
     * sample to form distal dendrite connections.
     *
     * @param learningRadius
     */
    public void setLearningRadius(int learningRadius) {
        paramMap.put(KEY.LEARNING_RADIUS, learningRadius);
    }

    /**
     * If the number of synapses active on a segment is at least this
     * threshold, it is selected as the best matching
     * cell in a bursting column.
     *
     * @param minThreshold
     */
    public void setMinThreshold(int minThreshold) {
        paramMap.put(KEY.MIN_THRESHOLD, minThreshold);
    }

    /**
     * The maximum number of synapses added to a segment during learning.
     *
     * @param maxNewSynapseCount
     */
    public void setMaxNewSynapseCount(int maxNewSynapseCount) {
        paramMap.put(KEY.MAX_NEW_SYNAPSE_COUNT, maxNewSynapseCount);
    }

    /**
     * Seed for random number generator
     *
     * @param seed
     */
    public void setSeed(int seed) {
        paramMap.put(KEY.SEED, seed);
    }

    /**
     * Initial permanence of a new synapse
     *
     * @param
     */
    public void setInitialPermanence(double initialPermanence) {
        paramMap.put(KEY.INITIAL_PERMANENCE, initialPermanence);
    }

    /**
     * If the permanence value for a synapse
     * is greater than this value, it is said
     * to be connected.
     *
     * @param connectedPermanence
     */
    public void setConnectedPermanence(double connectedPermanence) {
        paramMap.put(KEY.CONNECTED_PERMANENCE, connectedPermanence);
    }

    /**
     * Amount by which permanences of synapses
     * are incremented during learning.
     *
     * @param permanenceIncrement
     */
    public void setPermanenceIncrement(double permanenceIncrement) {
        paramMap.put(KEY.PERMANENCE_INCREMENT, permanenceIncrement);
    }

    /**
     * Amount by which permanences of synapses
     * are decremented during learning.
     *
     * @param permanenceDecrement
     */
    public void setPermanenceDecrement(double permanenceDecrement) {
        paramMap.put(KEY.PERMANENCE_DECREMENT, permanenceDecrement);
    }

    ////////////////////////////// SPACIAL POOLER PARAMS //////////////////////////////////

    /**
     * A list representing the dimensions of the input
     * vector. Format is [height, width, depth, ...], where
     * each value represents the size of the dimension. For a
     * topology of one dimension with 100 inputs use 100, or
     * [100]. For a two dimensional topology of 10x5 use
     * [10,5].
     *
     * @param inputDimensions
     */
    public void setInputDimensions(int[] inputDimensions) {
        paramMap.put(KEY.INPUT_DIMENSIONS, inputDimensions);
    }

    /**
     * This parameter determines the extent of the input
     * that each column can potentially be connected to.
     * This can be thought of as the input bits that
     * are visible to each column, or a 'receptiveField' of
     * the field of vision. A large enough value will result
     * in 'global coverage', meaning that each column
     * can potentially be connected to every input bit. This
     * parameter defines a square (or hyper square) area: a
     * column will have a max square potential pool with
     * sides of length 2 * potentialRadius + 1.
     *
     * @param potentialRadius
     */
    public void setPotentialRadius(int potentialRadius) {
        paramMap.put(KEY.POTENTIAL_RADIUS, potentialRadius);
    }

    /**
     * The inhibition radius determines the size of a column's local
     * neighborhood. of a column. A cortical column must overcome the overlap
     * score of columns in his neighborhood in order to become actives. This
     * radius is updated every learning round. It grows and shrinks with the
     * average number of connected synapses per column.
     *
     * @param    inhibitionRadius    the local group size
     */
    public void setInhibitionRadius(int inhibitionRadius) {
        paramMap.put(KEY.INHIBITION_RADIUS, inhibitionRadius);
    }

    /**
     * The percent of the inputs, within a column's
     * potential radius, that a column can be connected to.
     * If set to 1, the column will be connected to every
     * input within its potential radius. This parameter is
     * used to give each column a unique potential pool when
     * a large potentialRadius causes overlap between the
     * columns. At initialization time we choose
     * ((2*potentialRadius + 1)^(# inputDimensions) *
     * potentialPct) input bits to comprise the column's
     * potential pool.
     *
     * @param potentialPct
     */
    public void setPotentialPct(double potentialPct) {
        paramMap.put(KEY.POTENTIAL_PCT, potentialPct);
    }

    /**
     * If true, then during inhibition phase the winning
     * columns are selected as the most active columns from
     * the region as a whole. Otherwise, the winning columns
     * are selected with respect to their local
     * neighborhoods. Using global inhibition boosts
     * performance x60.
     *
     * @param globalInhibition
     */
    public void setGlobalInhibition(boolean globalInhibition) {
        paramMap.put(KEY.GLOBAL_INHIBITIONS, globalInhibition);
    }

    /**
     * The desired density of active columns within a local
     * inhibition area (the size of which is set by the
     * internally calculated inhibitionRadius, which is in
     * turn determined from the average size of the
     * connected potential pools of all columns). The
     * inhibition logic will insure that at most N columns
     * remain ON within a local inhibition area, where N =
     * localAreaDensity * (total number of columns in
     * inhibition area).
     *
     * @param localAreaDensity
     */
    public void setLocalAreaDensity(double localAreaDensity) {
        paramMap.put(KEY.LOCAL_AREA_DENSITY, localAreaDensity);
    }

    /**
     * An alternate way to control the density of the active
     * columns. If numActivePerInhArea is specified then
     * localAreaDensity must be less than 0, and vice versa.
     * When using numActivePerInhArea, the inhibition logic
     * will insure that at most 'numActivePerInhArea'
     * columns remain ON within a local inhibition area (the
     * size of which is set by the internally calculated
     * inhibitionRadius, which is in turn determined from
     * the average size of the connected receptive fields of
     * all columns). When using this method, as columns
     * learn and grow their effective receptive fields, the
     * inhibitionRadius will grow, and hence the net density
     * of the active columns will *decrease*. This is in
     * contrast to the localAreaDensity method, which keeps
     * the density of active columns the same regardless of
     * the size of their receptive fields.
     *
     * @param numActiveColumnsPerInhArea
     */
    public void setNumActiveColumnsPerInhArea(double numActiveColumnsPerInhArea) {
        paramMap.put(KEY.NUM_ACTIVE_COLUMNS_PER_INH_AREA, numActiveColumnsPerInhArea);
    }

    /**
     * This is a number specifying the minimum number of
     * synapses that must be on in order for a columns to
     * turn ON. The purpose of this is to prevent noise
     * input from activating columns. Specified as a percent
     * of a fully grown synapse.
     *
     * @param stimulusThreshold
     */
    public void setStimulusThreshold(double stimulusThreshold) {
        paramMap.put(KEY.STIMULUS_THRESHOLD, stimulusThreshold);
    }

    /**
     * The amount by which an inactive synapse is
     * decremented in each round. Specified as a percent of
     * a fully grown synapse.
     *
     * @param synPermInactiveDec
     */
    public void setSynPermInactiveDec(double synPermInactiveDec) {
        paramMap.put(KEY.SYN_PERM_INACTIVE_DEC, synPermInactiveDec);
    }

    /**
     * The amount by which an active synapse is incremented
     * in each round. Specified as a percent of a
     * fully grown synapse.
     *
     * @param synPermActiveInc
     */
    public void setSynPermActiveInc(double synPermActiveInc) {
        paramMap.put(KEY.SYN_PERM_ACTIVE_INC, synPermActiveInc);
    }

    /**
     * The default connected threshold. Any synapse whose
     * permanence value is above the connected threshold is
     * a "connected synapse", meaning it can contribute to
     * the cell's firing.
     *
     * @param synPermConnected
     */
    public void setSynPermConnected(double synPermConnected) {
        paramMap.put(KEY.SYN_PERM_CONNECTED, synPermConnected);
    }

    /**
     * Sets the increment of synapse permanences below the stimulus
     * threshold
     *
     * @param synPermBelowStimulusInc
     */
    public void setSynPermBelowStimulusInc(double synPermBelowStimulusInc) {
        paramMap.put(KEY.SYN_PERM_BELOW_STIMULUS_INC, synPermBelowStimulusInc);
    }

    /**
     * @param synPermTrimThreshold
     */
    public void setSynPermTrimThreshold(double synPermTrimThreshold) {
        paramMap.put(KEY.SYN_PERM_TRIM_THRESHOLD, synPermTrimThreshold);
    }

    /**
     * A number between 0 and 1.0, used to set a floor on
     * how often a column should have at least
     * stimulusThreshold active inputs. Periodically, each
     * column looks at the overlap duty cycle of
     * all other columns within its inhibition radius and
     * sets its own internal minimal acceptable duty cycle
     * to: minPctDutyCycleBeforeInh * max(other columns'
     * duty cycles).
     * On each iteration, any column whose overlap duty
     * cycle falls below this computed value will  get
     * all of its permanence values boosted up by
     * synPermActiveInc. Raising all permanences in response
     * to a sub-par duty cycle before  inhibition allows a
     * cell to search for new inputs when either its
     * previously learned inputs are no longer ever active,
     * or when the vast majority of them have been
     * "hijacked" by other columns.
     *
     * @param minPctOverlapDutyCycles
     */
    public void setMinPctOverlapDutyCycle(double minPctOverlapDutyCycles) {
        paramMap.put(KEY.MIN_PCT_OVERLAP_DUTY_CYCLE, minPctOverlapDutyCycles);
    }

    /**
     * A number between 0 and 1.0, used to set a floor on
     * how often a column should be activate.
     * Periodically, each column looks at the activity duty
     * cycle of all other columns within its inhibition
     * radius and sets its own internal minimal acceptable
     * duty cycle to:
     * minPctDutyCycleAfterInh *
     * max(other columns' duty cycles).
     * On each iteration, any column whose duty cycle after
     * inhibition falls below this computed value will get
     * its internal boost factor increased.
     *
     * @param minPctActiveDutyCycles
     */
    public void setMinPctActiveDutyCycle(double minPctActiveDutyCycles) {
        paramMap.put(KEY.MIN_PCT_ACTIVE_DUTY_CYCLE, minPctActiveDutyCycles);
    }

    /**
     * The period used to calculate duty cycles. Higher
     * values make it take longer to respond to changes in
     * boost or synPerConnectedCell. Shorter values make it
     * more unstable and likely to oscillate.
     *
     * @param dutyCyclePeriod
     */
    public void setDutyCyclePeriod(int dutyCyclePeriod) {
        paramMap.put(KEY.DUTY_CYCLE_PERIOD, dutyCyclePeriod);
    }

    /**
     * The maximum overlap boost factor. Each column's
     * overlap gets multiplied by a boost factor
     * before it gets considered for inhibition.
     * The actual boost factor for a column is number
     * between 1.0 and maxBoost. A boost factor of 1.0 is
     * used if the duty cycle is >= minOverlapDutyCycle,
     * maxBoost is used if the duty cycle is 0, and any duty
     * cycle in between is linearly extrapolated from these
     * 2 endpoints.
     *
     * @param maxBoost
     */
    public void setMaxBoost(double maxBoost) {
        paramMap.put(KEY.MAX_BOOST, maxBoost);
    }

    /**
     * spVerbosity level: 0, 1, 2, or 3
     *
     * @param spVerbosity
     */
    public void setSpVerbosity(int spVerbosity) {
        paramMap.put(KEY.SP_VERBOSITY, spVerbosity);
    }

    /////////////////// Encoder Params ///////////////////

    /**
     * Sets the "w" or width of the output signal
     * <em>Restriction:</em> w must be odd to avoid centering problems.
     *
     * @param w
     */
    public void setW(int w) {
        paramMap.put(KEY.W, w);
    }

    /**
     * The number of bits in the output. Must be greater than or equal to w
     *
     * @param n
     */
    public void setN(int n) {
        paramMap.put(KEY.N, n);
    }

    /**
     * The minimum value of the input signal.
     *
     * @param minVal
     */
    public void setMinVal(double minVal) {
        paramMap.put(KEY.MINVAL, minVal);
    }

    /**
     * The maximum value of the input signal.
     *
     * @param maxVal
     */
    public void setMaxVal(double maxVal) {
        paramMap.put(KEY.MAXVAL, maxVal);
    }

    /**
     * inputs separated by more than, or equal to this distance will have non-overlapping
     * representations
     *
     * @param radius
     */
    public void setRadius(double radius) {
        paramMap.put(KEY.RADIUS, radius);
    }

    /**
     * inputs separated by more than, or equal to this distance will have different
     * representations
     *
     * @param resolution
     */
    public void setResolution(double resolution) {
        paramMap.put(KEY.RESOLUTION, resolution);
    }

    /**
     * If true, non-periodic inputs smaller than minval or greater
     * than maxval will be clipped to minval/maxval
     *
     * @param b
     */
    public void setClipInput(boolean b) {
        paramMap.put(KEY.CLIP_INPUT, b);
    }

    /**
     * If true, then the input value "wraps around" such that minval = maxval
     * For a periodic value, the input must be strictly less than maxval,
     * otherwise maxval is a true upper bound.
     *
     * @param b
     */
    public void setPeriodic(boolean b) {
        paramMap.put(KEY.PERIODIC, b);
    }

    /**
     * If true, skip some safety checks (for compatibility reasons), default false
     *
     * @param b
     */
    public void setForced(boolean b) {
        paramMap.put(KEY.FORCED, b);
    }

    /**
     * An optional string which will become part of the description
     *
     * @param name
     */
    public void setName(String name) {
        paramMap.put(KEY.NAME, name);
    }

    ///////////////////////////////

    public void setCategoryList(List<String> l) {
        paramMap.put(KEY.CATEGORY_LIST, l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ Spatial\n")
        .append("\t").append("inputDimensions :  ").append(getParameterByKey(KEY.INPUT_DIMENSIONS)).append("\n")
        .append("\t").append("potentialRadius :  ").append(getParameterByKey(KEY.POTENTIAL_RADIUS)).append("\n")
        .append("\t").append("potentialPct :  ").append(getParameterByKey(KEY.POTENTIAL_PCT)).append("\n")
        .append("\t").append("globalInhibition :  ").append(getParameterByKey(KEY.GLOBAL_INHIBITIONS)).append("\n")
        .append("\t").append("inhibitionRadius :  ").append(getParameterByKey(KEY.INHIBITION_RADIUS)).append("\n")
        .append("\t").append("localAreaDensity :  ").append(getParameterByKey(KEY.LOCAL_AREA_DENSITY)).append("\n")
        .append("\t").append("numActiveColumnsPerInhArea :  ").append(getParameterByKey(
                KEY.NUM_ACTIVE_COLUMNS_PER_INH_AREA)).append("\n")
        .append("\t").append("stimulusThreshold :  ").append(getParameterByKey(KEY.STIMULUS_THRESHOLD)).append("\n")
        .append("\t").append("synPermInactiveDec :  ").append(getParameterByKey(KEY.SYN_PERM_INACTIVE_DEC)).append("\n")
        .append("\t").append("synPermActiveInc :  ").append(getParameterByKey(KEY.SYN_PERM_ACTIVE_INC)).append("\n")
        .append("\t").append("synPermConnected :  ").append(getParameterByKey(KEY.SYN_PERM_CONNECTED)).append("\n")
        .append("\t").append("synPermBelowStimulusInc :  ").append(getParameterByKey(KEY.SYN_PERM_BELOW_STIMULUS_INC)).append(
                "\n")
        .append("\t").append("minPctOverlapDutyCycles :  ").append(getParameterByKey(KEY.MIN_PCT_OVERLAP_DUTY_CYCLE)).append(
                "\n")
        .append("\t").append("minPctActiveDutyCycles :  ").append(getParameterByKey(KEY.MIN_PCT_ACTIVE_DUTY_CYCLE)).append(
                "\n")
        .append("\t").append("dutyCyclePeriod :  ").append(getParameterByKey(KEY.DUTY_CYCLE_PERIOD)).append("\n")
        .append("\t").append("maxBoost :  ").append(getParameterByKey(KEY.MAX_BOOST)).append("\n")
        .append("\t").append("spVerbosity :  ").append(getParameterByKey(KEY.SP_VERBOSITY)).append("\n")
        .append("}\n\n")
        
        .append("{ Temporal\n")
        .append("\t").append("activationThreshold :  ").append(getParameterByKey(KEY.ACTIVATION_THRESHOLD)).append("\n")
        .append("\t").append("cellsPerColumn :  ").append(getParameterByKey(KEY.CELLS_PER_COLUMN)).append("\n")
        .append("\t").append("columnDimensions :  ").append(getParameterByKey(KEY.COLUMN_DIMENSIONS)).append("\n")
        .append("\t").append("connectedPermanence :  ").append(getParameterByKey(KEY.CONNECTED_PERMANENCE)).append("\n")
        .append("\t").append("initialPermanence :  ").append(getParameterByKey(KEY.INITIAL_PERMANENCE)).append("\n")
        .append("\t").append("maxNewSynapseCount :  ").append(getParameterByKey(KEY.MAX_NEW_SYNAPSE_COUNT)).append("\n")
        .append("\t").append("minThreshold :  ").append(getParameterByKey(KEY.MIN_THRESHOLD)).append("\n")
        .append("\t").append("permanenceIncrement :  ").append(getParameterByKey(KEY.PERMANENCE_INCREMENT)).append("\n")
        .append("\t").append("permanenceDecrement :  ").append(getParameterByKey(KEY.PERMANENCE_DECREMENT)).append("\n")
        .append("}\n\n")
        
        .append("{ Encoder\n")
        .append("\t").append("w :  ").append(getParameterByKey(KEY.W)).append("\n")
        .append("\t").append("n :  ").append(getParameterByKey(KEY.N)).append("\n")
        .append("\t").append("radius :  ").append(getParameterByKey(KEY.RADIUS)).append("\n")
        .append("\t").append("resolution :  ").append(getParameterByKey(KEY.RESOLUTION)).append("\n")
        .append("\t").append("minval :  ").append(getParameterByKey(KEY.MINVAL)).append("\n")
        .append("\t").append("maxval :  ").append(getParameterByKey(KEY.MAXVAL)).append("\n")
        .append("\t").append("periodic :  ").append(getParameterByKey(KEY.PERIODIC)).append("\n")
        .append("\t").append("clipInput :  ").append(getParameterByKey(KEY.CLIP_INPUT)).append("\n")
        .append("\t").append("forced :  ").append(getParameterByKey(KEY.FORCED)).append("\n")
        .append("\t").append("name :  ").append(getParameterByKey(KEY.NAME)).append("\n")
        .append("}\n\n");

        return sb.toString();
    }

}
