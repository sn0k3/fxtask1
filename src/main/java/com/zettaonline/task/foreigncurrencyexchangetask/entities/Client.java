package com.zettaonline.task.foreigncurrencyexchangetask.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "clients")
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "client_id", nullable=false, unique=true, length = 100)
	private String clientIdentification;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	

	@OneToMany(mappedBy = "client")
	private List<Balance> balances = new ArrayList<>();

	@OneToMany(mappedBy = "client")
	private List<Conversion> conversions = new ArrayList<>();
	
	protected Client() {
		
	}
	
	public Client(String clientId) {
		this.clientIdentification = clientId;
	}
	
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getClientIdentification() {
		return this.clientIdentification;
	}
	
	public void setClientIdentification(String clientIdentification) {
		this.clientIdentification = clientIdentification;
	}
	
	
	public Instant getCreatedAt() {
        return createdAt;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public List<Conversion> getConversions() {
        return conversions;
    }
    
    @jakarta.persistence.PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
	
}
