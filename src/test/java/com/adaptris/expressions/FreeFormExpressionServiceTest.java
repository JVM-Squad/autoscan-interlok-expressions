/*
    Copyright Adaptris Ltd

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.adaptris.expressions;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.ServiceCase;
import com.adaptris.core.ServiceException;
import com.adaptris.core.common.MetadataDataOutputParameter;

public class FreeFormExpressionServiceTest extends ServiceCase {
  
  public FreeFormExpressionServiceTest(String name) {
    super(name);
  }

  private FreeFormExpressionService service;
  
  public void setUp() throws Exception {
    service = new FreeFormExpressionService();
  }

  public void tearDown() throws Exception {
    
  }
  
  public void testSimpleAlgorithmDefaultResultMetadataKey() throws Exception {
    service.setAlgorithm("10+10");
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    assertTrue(service.getResult() instanceof MetadataDataOutputParameter);
    assertEquals("20", adaptrisMessage.getMetadataValue("expressionResult"));
  }

  public void testInvalidFormat() throws Exception {
    service.setAlgorithm("x+x");
    
    try {
      execute(service, DefaultMessageFactory.getDefaultInstance().newMessage());
      fail("Should fail because the parameters are not numbers.");
    } catch (ServiceException ex) {
      // expected
    }
  }
  
  public void testCustomResult() throws Exception {
    service.setAlgorithm("20 + 20");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("40", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanEqualsResult() throws Exception {
    service.setAlgorithm("20 == 20");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanEqualsResolveResult() throws Exception {
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    adaptrisMessage.addMessageHeader("key1", "20");
    adaptrisMessage.addMessageHeader("key2", "20");
    
    service.setAlgorithm("%message{key1} == %message{key2}");
    
    execute(service, adaptrisMessage);
    
    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testComplexBooleanEqualsResolveResult() throws Exception {
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    adaptrisMessage.addMessageHeader("key1", "20");
    adaptrisMessage.addMessageHeader("key2", "20");
    
    service.setAlgorithm("(%message{key1} - 10 ) == ((%message{key2} - 5) - 5)");
    
    execute(service, adaptrisMessage);
    
    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testComplexBooleanNotEqualsResolveResult() throws Exception {
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    adaptrisMessage.addMessageHeader("key1", "20");
    adaptrisMessage.addMessageHeader("key2", "20");
    
    service.setAlgorithm("(%message{key1} - 10 ) == ((%message{key2} - 10) - 10)");
    
    execute(service, adaptrisMessage);
    
    assertEquals("false", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanNotEqualsResolveResult() throws Exception {
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    adaptrisMessage.addMessageHeader("key1", "10");
    adaptrisMessage.addMessageHeader("key2", "20");
    
    service.setAlgorithm("%message{key1} == %message{key2}");
    
    execute(service, adaptrisMessage);
    
    assertEquals("false", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanNotEqualsResult() throws Exception {
    service.setAlgorithm("50 == 20");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("false", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanGreaterResolveResult() throws Exception {
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    adaptrisMessage.addMessageHeader("key1", "30");
    adaptrisMessage.addMessageHeader("key2", "20");
    
    service.setAlgorithm("%message{key1} > %message{key2}");
    
    execute(service, adaptrisMessage);
    
    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanGreaterResult() throws Exception {
    service.setAlgorithm("30 > 20");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanNotGreaterResult() throws Exception {
    service.setAlgorithm("10 > 20");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("false", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanLessResult() throws Exception {
    service.setAlgorithm("10 < 20");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanNotLessResult() throws Exception {
    service.setAlgorithm("30 < 20");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("false", adaptrisMessage.getMetadataValue("customKey"));
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    ExpressionService service = new ExpressionService();
    service.setResultFormatter(new BooleanResultFormatter());
    service.setAlgorithm("(10 > %message{key1})");
    return service;

  }
}
