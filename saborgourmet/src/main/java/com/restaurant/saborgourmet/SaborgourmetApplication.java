package com.restaurant.saborgourmet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableAspectJAutoProxy
public class SaborgourmetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaborgourmetApplication.class, args);
		System.out.println("\n===========================================");
		System.out.println("🍽️  SABOR GOURMET - Sistema Iniciado");
		System.out.println("===========================================");
		System.out.println("📍 URL: http://localhost:8080");
		System.out.println("🔐 Login: http://localhost:8080/login");
		System.out.println("👤 Usuario: admin / Contraseña: Admin123");
		System.out.println("===========================================\n");
	}

}
