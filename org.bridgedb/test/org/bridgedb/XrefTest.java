/*
 *BridgeDb,
 *An abstraction layer for identifier mapping services, both local and online.
 *Copyright (c) 2013 Egon Willighagen <egonw@users.sf.net>
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and limitations under the License.
 */
package org.bridgedb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class XrefTest {

    //Oct 29, 2013 Name changed to match that used elsewhere
	private final DataSource EN = DataSource.register("En", "Ensembl").asDataSource();
	private final DataSource UNIPROT = DataSource.register("S", "Uniprot-TrEMBL").
		urnBase("urn:miriam:uniprot:").asDataSource();

	@Test
	public void testConstructor() {
		Xref xref = new Xref("ENSG000001", EN);
		assertNotNull(xref);
		assertEquals("ENSG000001", xref.getId());
		assertNotNull(xref.getDataSource());
		assertEquals("En", xref.getDataSource().getSystemCode());
	}

	@Test
	public void testEquals_Null() {
		Xref xref = new Xref("ENSG000001", EN);
		assertNotEquals(null, xref);
	}

	@Test
	public void testEquals_NonXref() {
		Xref xref = new Xref("ENSG000001", EN);
		assertNotEquals("ENSG000001", xref);
	}

	@Test
	public void testEquals_DiffId() {
		Xref xref = new Xref("ENSG000001", EN);
		Xref xref2 = new Xref("ENSG000002", EN);
		assertNotEquals(xref, xref2);
		assertNotEquals(xref2, xref); // and symmetric
	}

	@Test
	public void testEquals() {
		Xref xref = new Xref("ENSG000001", EN);
		Xref xref2 = new Xref("ENSG000001", EN);
		assertEquals(xref, xref2);
		assertEquals(xref2, xref); // and symmetric
		Xref xref3 = new Xref("ENSG000001", EN, true);
		Xref xref4 = new Xref("ENSG000001", EN, false);
		assertNotEquals(xref3, xref4);
	}

	@Test
	public void testCompareTo() {
		Xref xref1 = new Xref("ENSG000001", EN);
		Xref xref2 = new Xref("ENSG000001", EN);
		assertEquals(0, xref1.compareTo(xref2));
		assertEquals(0, xref2.compareTo(xref1)); // and symmetric
		Xref xref3 = new Xref("ENSG000001", EN, true);
		Xref xref4 = new Xref("ENSG000001", EN, false);
		assertNotEquals(xref3.isPrimary(), xref4.isPrimary());
		assertEquals(xref3, xref4);
		assertEquals(xref4, xref3); // and symmetric
	}

	@Test
	public void testCompareTo_Diff() {
		Xref xref = new Xref("ENSG000001", EN);
		Xref xref2 = new Xref("ENSG000002", EN);
		assertNotSame(0, xref.compareTo(xref2));
		assertNotSame(0, xref2.compareTo(xref)); // and symmetric
	}

	@Test
	public void testURNRoundtripping() {
		Xref xref = new Xref("P12345", UNIPROT);
		String urn = xref.getURN();
		assertNotNull(urn);
		assertNotSame(0, urn.length());
		Xref xref2 = Xref.fromUrn(urn);
		assertEquals(xref, xref2);
		assertEquals(xref2, xref); // and symmetric
	}
}
