package examples.hibernate.vector;

import jakarta.persistence.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

/**
 * PostgreSQL/pgvector-oriented source example. It intentionally lives outside the application's
 * entity-scan package so the zero-setup H2 profile remains runnable. Move it into the persistence
 * package (and enable the vector extension) for a PostgreSQL vector-search deployment.
 */
@Entity
@Table(name = "document_embeddings")
public class DocumentEmbeddingMappingExample {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String text;
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 384)
    private float[] embedding;

    protected DocumentEmbeddingMappingExample() {
    }

    public DocumentEmbeddingMappingExample(UUID id, String text, float[] embedding) {
        this.id = id;
        this.text = text;
        this.embedding = embedding;
    }
}
