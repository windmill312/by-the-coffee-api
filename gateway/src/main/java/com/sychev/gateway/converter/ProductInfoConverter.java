package com.sychev.gateway.converter;

import com.sychev.common.grpc.model.GPage;
import com.sychev.gateway.web.to.common.PagedResult;
import com.sychev.gateway.web.to.in.AddProductRequest;
import com.sychev.gateway.web.to.in.UpdateProductRequest;
import com.sychev.gateway.web.to.out.ProductInfo;
import com.sychev.gateway.web.to.out.common.ProductGroup;
import com.sychev.product.grpc.model.v1.GAddProductRequest;
import com.sychev.product.grpc.model.v1.GProductInfo;
import com.sychev.product.grpc.model.v1.GUpdateProductRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ProductInfoConverter {

    public static PagedResult<ProductInfo> convert(
            List<GProductInfo> productInfoList, GPage page) {
        return new PagedResult<>(
                page.getTotalElements(),
                productInfoList.stream()
                        .map(ProductInfoConverter::convert)
                        .collect(Collectors.toList()));
    }

    public static ProductInfo convert(GProductInfo productInfo) {
        return new ProductInfo(
                CommonConverter.convert(productInfo.getProductUid()),
                productInfo.getName(),
                productInfo.getDescription(),
                productInfo.getPrice(),
                convert(productInfo.getGroup()));
    }

    private static ProductGroup convert(GProductInfo.GProductGroup group) {
        return ProductGroup.valueOf(group.name());
    }

    private static GProductInfo.GProductGroup convert(ProductGroup group) {
        return GProductInfo.GProductGroup.valueOf(group.name());
    }

    public static GAddProductRequest convert(AddProductRequest request) {
        return GAddProductRequest.newBuilder()
                .setProduct(GProductInfo.newBuilder()
                        .setGroup(convert(request.getProductGroup()))
                        .setName(request.getName())
                        .setPrice(request.getPrice())
                        .setDescription(request.getDescription())
                )
                .build();
    }

    public static GUpdateProductRequest convert(UpdateProductRequest request) {
        return GUpdateProductRequest.newBuilder()
                .setProduct(GProductInfo.newBuilder()
                        .setProductUid(CommonConverter.convert(request.getProductUid()))
                        .setGroup(convert(request.getProductGroup()))
                        .setName(request.getName())
                        .setPrice(request.getPrice())
                        .setDescription(request.getDescription())
                )
                .build();
    }
}
