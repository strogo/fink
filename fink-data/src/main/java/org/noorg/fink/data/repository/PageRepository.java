package org.noorg.fink.data.repository;

import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.noorg.fink.data.entities.Page;
import org.noorg.fink.data.entities.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.DirectGraphRepositoryFactory;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.GraphDatabaseContext;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableList;

@Repository
public class PageRepository {

	private GraphRepository<Page> repository;

	private GraphDatabaseContext context;

	@Autowired
	public void setGraphRepositoryFactory(GraphDatabaseContext ctx,
			DirectGraphRepositoryFactory graphRepositoryFactory) {
		repository = graphRepositoryFactory.createGraphRepository(Page.class);
		context = ctx;
	}

	public void createSomePages() {
		Page p = new Page("root");
		p.addPage(new Page("sub1"));
		p.addPage(new Page("sub2"));
		p.addTag(new Tag("tagged"));
		p.addTag(new Tag("by"));
		p.addTag(new Tag("artist"));
		repository.save(p);
	}

	public void save(Page p) {
		repository.save(p);
	}

	public long countPages() {
		return repository.count();
	}

	public Page findPageByUuid(String uuid) {
		return repository.findByPropertyValue("uuid", uuid);
	}

	public Page findPageByTitle(String title) {
		return repository.findByPropertyValue("title", title);
	}

	public Page findPageByTitleManually(String title) {
		Index<Node> index = context.getIndex(Page.class, "index_title");
		IndexHits<Node> hits = index.get("title", title);
		if (hits.hasNext()) {
			return context.createEntityFromState(hits.next(), Page.class);
		}
		return null;
	}

	public Page find(String property, String value) {
		return repository.findByPropertyValue(property, value);
	}

	public Page findById(long id) {
		return repository.findOne(id);
	}

	public List<Page> findAll() {
		return ImmutableList.copyOf(repository.findAll());
	}

}