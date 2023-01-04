package ro.aesm.qc.meta.misc.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import ro.aesm.qc.api.meta.component.IMetaComponentModel;
import ro.aesm.qc.base.util.IOUtils;
import ro.aesm.qc.meta.misc.txt.TxtModel;
import ro.aesm.qc.meta.misc.txt.TxtParser;
import ro.aesm.qc.meta.misc.txt.TxtProcessor;

class Test_TxtProcessor {

	@Test
	public void testProcessor() throws Exception {

		String metaLocation = "ro/aesm/qc/meta/misc/tests/txtprocessor_meta.xml";
		String dataLocation = "ro/aesm/qc/meta/misc/tests/txtprocessor_data.txt";

		InputStream is = this.getClass().getClassLoader().getResourceAsStream(metaLocation);
		TxtParser parser = new TxtParser();
		Document doc = parser.getXmlDocument(is);
		Map<String, IMetaComponentModel> modelMap = parser.parseChildrenAsMap(doc.getDocumentElement());

		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(dataLocation)) {
			TxtProcessor srv = new TxtProcessor();
			String txt = IOUtils.toString(inputStream);
			String result;

			result = srv.execute((TxtModel) modelMap.get("keep"), txt);
			assertEquals("<li>a</li>|<li>b</li>|<li>c</li>|<li>d</li>", result, "keep1");

			result = srv.execute((TxtModel) modelMap.get("keep2"), txt);
			assertEquals("<li>a</li>|<li>c</li>", result, "keep2");

			result = srv.execute((TxtModel) modelMap.get("keep3"), txt);
			assertEquals("<li>b</li>|<li>c</li>", result, "keep3");

			result = srv.execute((TxtModel) modelMap.get("keep3.1"), txt);
			assertEquals("<li>b</li>|<li>c</li>|<li>d</li>", result, "keep3.1");

			result = srv.execute((TxtModel) modelMap.get("keep4"), txt);
			assertEquals("<li>a</li>", result, "keep4");

			result = srv.execute((TxtModel) modelMap.get("keep5"), txt);
			assertEquals("ul", result, "keep5");

			result = srv.execute((TxtModel) modelMap.get("keep_id"), txt);
			assertEquals("ul1", result, "keep_id");

			result = srv.execute((TxtModel) modelMap.get("demo_replace_default_occurrence"), txt);
			assertEquals("<ul id=\"ul1\"><x>a</li><li>b</li><li>c</li><li>d</li></ul>", result,
					"demo_replace_default_occurrence");

			result = srv.execute((TxtModel) modelMap.get("demo_replace_all"), txt);
			assertEquals("<ul id=\"ul1\"><x>a</x><x>b</x><x>c</x><x>d</x></ul>", result, "demo_replace_all");

			result = srv.execute((TxtModel) modelMap.get("demo_replace_list"), txt);
			assertEquals("<ul id=\"ul1\"><li>a</x><x>b</li><li>c</li><li>d</li></ul>", result, "demo_replace_list");

			result = srv.execute((TxtModel) modelMap.get("demo_replace_range"), txt);
			assertEquals("<ul id=\"ul1\"><li>a</li><x>b</x><x>c</x><x>d</x></ul>", result, "demo_replace_range");

			result = srv.execute((TxtModel) modelMap.get("demo_replace_index_range"), txt);
			assertEquals("<ul -><li>a</li><li>b</li><li>c</li><li>d</li></ul>", result, "demo_replace_index_range");

			result = srv.execute((TxtModel) modelMap.get("demo_replace_between"), txt);
			assertEquals("<ul id=\"ul1\">[x][x][x][x]</ul>", result, "demo_replace_between");

			result = srv.execute((TxtModel) modelMap.get("demo_replace_between_with_occurrence"), txt);
			assertEquals("<ul id=\"ul1\">x<li>b</li>x<li>d</li></ul>", result, "demo_replace_between_with_occurrence");

		}
	}

}
