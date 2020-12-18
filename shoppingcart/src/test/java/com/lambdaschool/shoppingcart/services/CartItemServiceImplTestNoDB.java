package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.ShoppingCartApplication;
import com.lambdaschool.shoppingcart.ShoppingCartApplicationTest;
import com.lambdaschool.shoppingcart.exceptions.ResourceNotFoundException;
import com.lambdaschool.shoppingcart.models.*;
import com.lambdaschool.shoppingcart.repository.CartItemRepository;
import com.lambdaschool.shoppingcart.repository.ProductRepository;
import com.lambdaschool.shoppingcart.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingCartApplicationTest.class,
properties = {"command.line.runner.enabled=false"})
public class CartItemServiceImplTestNoDB
{
    @Autowired
    CartItemService cartitemService;

    @MockBean
    ProductRepository productrepos;

    @MockBean
    CartItemRepository cartItemRepository;

    @MockBean
    UserRepository userrepos;

    private List<User> userList = new ArrayList<>();

    private List<Product> productList = new ArrayList<>();

    private List<CartItem> cartItemsList = new ArrayList<>();

    @Before
    public void setUp() throws Exception
    {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");

        r1.setRoleid(9);
        r2.setRoleid(10);

        User u1 = new User("barnbarn",
                "LambdaLlama",
                "barnbarn@host.local",
                "");
        u1.setUserid(4);

        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));

        userList.add(u1);

        User u2 = new User("cinnamon",
                "LambdaLlama",
                "cinnamon@host.local",
                "");

        u2.setUserid(5);

        u2.getRoles().add(new UserRoles(u2, r2));

        userList.add(u2);

        User u3 = new User("stumps",
                "LambdaLlama",
                "stumps@host.local",
                "");

        u3.setUserid(6);

        u3.getRoles().add(new UserRoles(u3, r2));

        userList.add(u3);

        Product p1 = new Product();
        p1.setName("pen");
        p1.setDescription("makes words");
        p1.setPrice(2.50);
        p1.setComments("");

        p1.setProductid(1);

        productList.add(p1);

        Product p2 = new Product();
        p2.setName("pencil");
        p2.setDescription("does math");
        p2.setPrice(1.50);
        p2.setComments("");

        p2.setProductid(2);

        productList.add(p2);

        Product p3 = new Product();
        p3.setName("coffee");
        p3.setDescription("everyone needs coffee");
        p3.setPrice(4.00);
        p3.setComments("");

        p3.setProductid(3);

        productList.add(p3);

        CartItem ci1 = new CartItem(u1, p1, 4, "");
        CartItem ci2 = new CartItem(u1, p2, 3, "");
        CartItem ci3 = new CartItem(u1, p3, 2, "");
        CartItem ci4 = new CartItem(u2, p3, 1, "");
        CartItem ci5 = new CartItem(u3, p3, 17, "");

        cartItemsList.add(ci1);
        cartItemsList.add(ci2);
        cartItemsList.add(ci3);
        cartItemsList.add(ci4);
        cartItemsList.add(ci5);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void addToCart()
    {
        CartItem cartItem = new CartItem();

        String pen = "pen";
        cartItem.setProduct(productList.get(0));
        cartItem.setUser(userList.get(0));
        cartItem.setQuantity(2L);

        Mockito.when(userrepos.findById(userList.get(0).getUserid()))
                .thenReturn(Optional.of(userList.get(0)));

        Mockito.when(productrepos.findById(productList.get(0).getProductid()))
                .thenReturn(Optional.of(productList.get(0)));

        Mockito.when(cartItemRepository.findById(new CartItemId(userList.get(0).getUserid(), productList.get(0).getProductid())))
                .thenReturn(Optional.of(cartItem));

        Mockito.when(cartItemRepository.save(any(CartItem.class)))
                .thenReturn(cartItem);

        CartItem testCI = cartitemService.addToCart(userList.get(0).getUserid(), productList.get(0).getProductid(), "");

        assertNotNull(testCI);
        assertEquals(3L, testCI.getQuantity());
    }

    @Test
    public void removeFromCart()
    {
        CartItem cartItem = new CartItem();

        cartItem.setUser(userList.get(0));
        cartItem.setProduct(productList.get(0));
        cartItem.setQuantity(5L);

        Mockito.when(userrepos.findById(userList.get(0).getUserid()))
                .thenReturn(Optional.of(userList.get(0)));

        Mockito.when(productrepos.findById(productList.get(0).getProductid()))
                .thenReturn(Optional.of(productList.get(0)));

        Mockito.when(cartItemRepository.findById(new CartItemId(userList.get(0).getUserid(), productList.get(0).getProductid())))
                .thenReturn(Optional.of(cartItem));

        Mockito.when(cartItemRepository.save(any(CartItem.class)))
                .thenReturn(cartItem);

        CartItem testCI = cartitemService.removeFromCart(userList.get(0).getUserid(), productList.get(0).getProductid(), "");

        assertNotNull(testCI);
        assertEquals(4L, testCI.getQuantity());
    }
}