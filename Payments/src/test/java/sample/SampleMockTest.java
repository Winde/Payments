package sample;

import main.ApplicationTestModule;
import model.dataobjects.Payment;
import model.persistence.PaymentRepository;
import model.persistence.TagRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import web.controllers.TagsController;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTestModule.class)
//@ContextConfiguration
//@WebAppConfiguration
public class SampleMockTest {

	public final String ANY_ID = "5";
	public final long ANY_ID_LONG = 5;
	
	@InjectMocks 
	private TagsController tagsController = new TagsController();
	
	@Mock
	private TagRepository tagRepositoryMock;
	
	@Mock
	private PaymentRepository paymentsRepositoryMock;

	
	@Before
	public void setUp() throws Exception {	
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testTagsForPaymentPointless() throws Exception {
		
		Payment expectedPayment = mock(Payment.class);
		//when(expectedPayment.equals(expectedPayment)).thenReturn(true);
		
		when(paymentsRepositoryMock.findOne(ANY_ID_LONG)).thenReturn(expectedPayment);
		
			
		ExtendedModelMap modelMap = new ExtendedModelMap();
		Payment returnPayment = tagsController.getTagsForPayment(ANY_ID, modelMap);
		
		assertEquals(expectedPayment, returnPayment);
		
	}
}
