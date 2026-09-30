package uo.ri.cws.application.service.acceptance.util.dbfixture;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TClientsRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TInterventionsRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TMechanicsRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TVehicleTypesRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TVehiclesRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TWorkOrdersRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInterventionsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVehicleTypesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVehiclesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;

public class DbFixtures {

	public static TMechanicsRecord aMechanic() {
		TMechanicsRecord mechanic = new TMechanicsRecordBuilder().build();
		Db.insert(mechanic);
		return mechanic;
	}

	public static TMechanicsRecord aMechanicOf(String nif) {
		
		TMechanicsRecord mechanic = new TMechanicsRecordBuilder()
				.withNif(nif)
				.build();
		Db.insert(mechanic);
		return mechanic;
	}

	public static TMechanicsRecord aMechanicOf(String nif, 
			String name, String surname) {
		
		TMechanicsRecord mechanic = new TMechanicsRecordBuilder()
				.withNif(nif)
				.withName(name)
				.withSurname(surname)
				.build();
		Db.insert(mechanic);
		return mechanic;
	}

	public static TMechanicsRecord aMechanicWithWorkOrders() {
		TMechanicsRecord m = aMechanic();
		aWorkOrderForMechanic(m.id);
		aWorkOrderForMechanic(m.id);
		return m;
	}

	public static TMechanicsRecord aMechanicWithInterventions() {
		TMechanicsRecord m = aMechanic();
		anInterventionForMechanic(m.id);
		anInterventionForMechanic(m.id);
		return m;
	}

	public static TInterventionsRecord anInterventionForMechanic(String mId) {
		TInterventionsRecord i = new TInterventionsRecordBuilder()
				.forMechanicId( mId )
				.build();
		
		Db.insert( i );
		return i;
	}

	public static TWorkOrdersRecord aWorkOrderForMechanic(String mId) {
		TWorkOrdersRecord wo1 = new TWorkOrdersRecordBuilder()
				.forMechanicId(mId)
				.build();
		
		Db.insert( wo1 );
		return wo1;
	}

	public static TWorkOrdersRecord aClientWithVehicleAndOneApprovedWorkorder() {
		TClientsRecord client = aClient();
		TVehiclesRecord vehicle = aVehicleForClient(client.id);
		TWorkOrdersRecord wo = aWorkOrderApprovedForVehicle(vehicle.id);
		return wo;
	}

	public static List<TWorkOrdersRecord> someWorkordersApprovedWithVehicleAndClient() {
		TClientsRecord client = aClient();
		return someWorkordersApprovedWithVehicleForClient(client.id);
	}
	
	
	public static List<TWorkOrdersRecord> someWorkordersFinishedWithVehicleAndClient() {
		TClientsRecord client = aClient();
		return someWorkordersFinishedWithVehicleForClient(client.id);
	}

	public static List<TWorkOrdersRecord> someWorkordersFinishedWithVehicleForClient(
			String clientId) {

		TVehiclesRecord vehicle = aVehicleForClient(clientId);
		return List.of(
				aWorkOrderApprovedForVehicle(vehicle.id),
				aWorkOrderApprovedForVehicle(vehicle.id),
				aWorkOrderApprovedForVehicle(vehicle.id)
			);
	}

	public static List<TWorkOrdersRecord> someWorkordersApprovedWithVehicleForClient(
			String clientId) {

		TVehiclesRecord vehicle = aVehicleForClient(clientId);
		return List.of(
				aWorkOrderApprovedForVehicle(vehicle.id),
				aWorkOrderApprovedForVehicle(vehicle.id),
				aWorkOrderApprovedForVehicle(vehicle.id)
			);
	}

	
	public static TWorkOrdersRecord aWorkOrderApprovedForVehicle(String vId) {
		TWorkOrdersRecord wo = aWorkOrderApprovedForVehicleOf(vId, new BigDecimal("100.0"));
		return wo;
	}

	public static TWorkOrdersRecord aWorkOrderOpenForVehicle(String vId) {
		return aWorkOrderForVehicleOf(vId, "OPEN");
	}

	public static TWorkOrdersRecord aWorkOrderInvoicedForVehicle(String vId) {
		return aWorkOrderForVehicleOf(vId, "INVOICED");
	}

	public static TWorkOrdersRecord aWorkOrderAssignedForVehicle(String vId) {
		sleep(10); // to ensure different timestamps
		
		TMechanicsRecord m = aMechanic();
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(vId)
				.forMechanicId(m.id)
				.withState("ASSIGNED")
				.build();
		Db.insert(wo);
		return wo;
	}

	public static TWorkOrdersRecord aWorkOrderFinishedForVehicle(String vId) {
		sleep(10); // to ensure different timestamps
		
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(vId)
				.withState("FINISHED")
				.build();
		Db.insert(wo);
		return wo;
	}
	
	public static TWorkOrdersRecord aWorkOrderApprovedForVehicleOf(String vId,
			BigDecimal bigDecimal) {
		
		sleep(10); // to ensure different timestamps
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(vId)
				.withState( "APPROVED" )
				.withAmount( bigDecimal )
				.build();
		Db.insert(wo);
		return wo;
	}
	
	public static TWorkOrdersRecord aWorkOrderForVehicleOf(String vId,
			String state) {
		
		sleep(10); // to ensure different timestamps
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(vId)
				.withState( state )
				.build();
		Db.insert(wo);
		return wo;
	}

	public static TVehiclesRecord aVehicleForClient(String id) {
		TVehicleTypesRecord vt = aVehicleType();
		TVehiclesRecord vehicle = new TVehiclesRecordBuilder()
				.forClientId(id)
				.forVehicleTypeId(vt.id)
				.build();
		Db.insert(vehicle);
		return vehicle;
	}

	public static TVehicleTypesRecord aVehicleType() {
		TVehicleTypesRecord vt = new TVehicleTypesRecordBuilder().build();
		Db.insert(vt);
		return vt;
	}

	public static TClientsRecord aClient() {
		TClientsRecord client = new TClientsRecordBuilder().build();
		Db.insert(client);
		return client;
	}

	private static void sleep(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static List<TWorkOrdersRecord> someWorkOrdersInvoicedWithInterventionsForMechanicOf(
	        String mechanicId,
	        BigDecimal invoicedAmount,
	        LocalDate firstDayOfLastMonth) {

	    BigDecimal third = invoicedAmount
	            .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_EVEN);

	    return List.of(
	            aWorkOrderInvoicedWithInterventionForMechanicOf(
	                    mechanicId,
	                    third,
	                    firstDayOfLastMonth.plusDays(1)
	            ),
	            aWorkOrderInvoicedWithInterventionForMechanicOf(
	                    mechanicId,
	                    third,
	                    firstDayOfLastMonth.plusDays(10)
	            ),
	            aWorkOrderInvoicedWithInterventionForMechanicOf(
	                    mechanicId,
	                    third,
	                    firstDayOfLastMonth.plusDays(20)
	            )
	    );
	}
	
	public static TWorkOrdersRecord aWorkOrderInvoicedWithInterventionForMechanicOf(
			String mechanicId, 
			BigDecimal amount,
			LocalDate date) {
		TVehiclesRecord v = aVehicleForClient( aClient().id );
		Timestamp invoiceDate = Timestamp.valueOf(date.atTime(12, 30));
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(v.id)
				.withAmount(amount)
				.withDate( invoiceDate )
				.invoiced()
				.build();
		Db.insert( wo );
		anInterventionForMechanicAndWorkOrder(mechanicId, wo.id, invoiceDate);
		return wo;
	}

	public static TInterventionsRecord anInterventionForMechanicAndWorkOrder(
			String mechanicId, String workOrderId, Timestamp invoiceDate) {

		TInterventionsRecord i = new TInterventionsRecordBuilder()
				.forMechanicId( mechanicId )
				.forWorkOrderId( workOrderId )
				.withMinutes( 60 )
				.withDate( invoiceDate )
				.build();		
		Db.insert( i );
		return i;
	}


}