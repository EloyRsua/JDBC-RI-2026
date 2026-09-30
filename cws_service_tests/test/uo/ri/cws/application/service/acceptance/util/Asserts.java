package uo.ri.cws.application.service.acceptance.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TMechanicsRecord;

public class Asserts {

	public static void matches(MechanicDto expected, MechanicDto dto) {
		assertEquals(expected.nif, dto.nif);
		assertEquals(expected.name, dto.name);
		assertEquals(expected.surname, dto.surname);
	}

	public static void recordMatchesDto(MechanicDto expected, TMechanicsRecord rec) {
		assertEquals(expected.id, rec.id);
		assertEquals(expected.nif, rec.nif);
		assertEquals(expected.name, rec.name);
		assertEquals(expected.surname, rec.surname);
		assertEquals(expected.version, rec.version);
	}

	public static void dtoMatchesRecord(TMechanicsRecord expected, MechanicDto dto) {
		assertEquals(expected.id, dto.id);
		assertEquals(expected.nif, dto.nif);
		assertEquals(expected.name, dto.name);
		assertEquals(expected.surname, dto.surname);
		assertEquals(expected.version, dto.version);
	}

	public static void isNow(Timestamp ts) {
        LocalDateTime ts1 = ts.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime ts2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        long difference = Math.abs(ts1.until(ts2, ChronoUnit.SECONDS));
        
        assertTrue(difference <= 1, "Difference is greater than 1 sec");
    }

	public static void recordMatchesDto(InvoiceDto expected, TInvoicesRecord loaded) {
		assertEquals(expected.id, loaded.id);
		assertEquals(expected.date, loaded.date.toLocalDate());
		
		assertEquals(0, expected.total.compareTo(loaded.total) );
		assertEquals(0, expected.subtotal.compareTo(loaded.subtotal));
		assertEquals(0, expected.vatAmount.compareTo(loaded.vatamount));
		assertEquals(0, expected.vatPercentage.compareTo(loaded.vatrate));

		assertEquals(expected.state, loaded.state);
		assertEquals(expected.version, loaded.version);
	}

	public static void rightAmounts(InvoiceDto expected, InvoiceDto actual) {
		assertEquals(0, expected.total.compareTo(actual.total));
		assertEquals(0, expected.subtotal.compareTo(actual.subtotal));
		assertEquals(0, expected.vatAmount.compareTo(actual.vatAmount));
		assertEquals(0, expected.vatPercentage.compareTo(actual.vatPercentage));

		assertEquals(expected.state, actual.state);
	}
}
