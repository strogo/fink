package org.noorg.fink.data;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noorg.fink.data.entities.Image;
import org.noorg.fink.data.entities.MediaCollection;
import org.noorg.fink.data.entities.Page;
import org.noorg.fink.data.entities.Tag;
import org.noorg.fink.data.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.GraphDatabaseContext;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = "/spring/testContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CollectionTest {

	@Autowired
	private MediaRepository mediaRepository;

	@Autowired
	private GraphDatabaseContext graphDatabaseContext;

	@Rollback(false)
	@BeforeTransaction
	public void clearDatabase() {
		Neo4jHelper.cleanDb(graphDatabaseContext);
	}

	private void createSomeCollections() {
		MediaCollection c = new MediaCollection("test");
		c.addItem(new Image("a", "a", "a", "a"));
		c.addItem(new Image("a", "a", "a", "a"));
		c.addItem(new Image("a", "a", "a", "a"));
		c.addTag(new Tag("tagged"));
		c.addTag(new Tag("by"));
		c.addTag(new Tag("artist"));
		mediaRepository.save(c);
	}
	
	@Test
	public void shouldCreatePages() {
		createSomeCollections();
		assertEquals(1, mediaRepository.countCollections());
	}

	@Test
	public void shouldIndexTitle() {
		createSomeCollections();
		MediaCollection c = mediaRepository.findCollectionByTitle("test");
		assertNotNull(c);
	}

	@Test
	public void shouldContainSubPages() {
		createSomeCollections();
		MediaCollection c = mediaRepository.findCollectionByTitle("test");
		assertNotNull(c);
		assertEquals(3, c.getItems().size());
		
		Iterator<Image> it = c.getItems().iterator();
		
		assertEquals(it.next().getTitle(), "a");
		assertEquals(it.next().getTitle(), "a");
		assertEquals(it.next().getTitle(), "a");
	}
		
	@Test
	public void shouldHaveTags() {
		createSomeCollections();
		MediaCollection c = mediaRepository.findCollectionByTitle("test");
		assertEquals(3, c.getTags().size());
	}
	
}
