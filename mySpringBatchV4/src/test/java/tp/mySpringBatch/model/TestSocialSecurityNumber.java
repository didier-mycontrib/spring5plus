package tp.mySpringBatch.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestSocialSecurityNumber {

  @Test
  public void testSocialSecurityNumberWithKey() {
	  String ssn1WithKeyAsString = "201020300400565";
	  System.out.println("ssn1WithKeyAsString=" + ssn1WithKeyAsString);
	  SocialSecurityNumber ssn1 = new SocialSecurityNumber(ssn1WithKeyAsString); 
	  System.out.println("ssn1=" + ssn1);
	  System.out.println("ssn1_computed_key=" + ssn1.computeKey());
	  assertEquals(ssn1.computeKey(),ssn1.getClef());
	  assertEquals(ssn1.toGlobalStringWhithKey(),ssn1WithKeyAsString);
  }
  
  @Test
  public void testSocialSecurityNumberWithoutKey() {
	  String ssn1WithoutKeyAsString = "2010203004005";
	  System.out.println("ssn1WithoutKeyAsString=" + ssn1WithoutKeyAsString);
	  SocialSecurityNumber ssn1 = new SocialSecurityNumber(ssn1WithoutKeyAsString); 
	  System.out.println("ssn1=" + ssn1);
	  System.out.println("ssn1_computed_key=" + ssn1.computeKey());
	  assertEquals(ssn1.computeKey(),ssn1.getClef());
	  assertEquals(ssn1.toGlobalStringWhithoutKey(),ssn1WithoutKeyAsString);
  }

}
