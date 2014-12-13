package data;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


public class AttributeTest 
{
    private static final String ATTR_NAME = "Color";
    private static final List<String> NOMINAL_VALUES = ImmutableList.of("Red", "Yellow", "Blue");
    private static final List<Integer> NOMINAL_IDS = ImmutableList.of(0, 1, 2);

    private static final Map<String, Integer> NOMINAL_VALUE_IDS = generateNominalValueIds();
    
    @Before
    public void before()
    {
        
    }
    
    @Test
    public void test_Constructor()
    {
        Attribute nominalAttr = new Attribute(ATTR_NAME, Attribute.Type.NOMINAL, (String[]) NOMINAL_VALUES.toArray());
        
        assertEquals(nominalAttr.getName(), ATTR_NAME);
        
        //assertEquals(nominalAttr.getNominalValueId(attrValueName), NOMINAL_VALUE_IDS.keySet());
        assertEquals(nominalAttr.getNominalValueMap().values(), NOMINAL_VALUE_IDS.values());
    }
    
    private static Map<String, Integer> generateNominalValueIds()
    {
        return  new ImmutableMap.Builder<String, Integer>()
                .put(NOMINAL_VALUES.get(0), NOMINAL_IDS.get(0))
                .put(NOMINAL_VALUES.get(1), NOMINAL_IDS.get(1))
                .put(NOMINAL_VALUES.get(2), NOMINAL_IDS.get(2))
                .build();
    }
    
}
