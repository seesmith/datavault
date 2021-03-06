<#import "*/layout/defaultlayout.ftl" as layout>
<@layout.vaultLayout>
    <#import "/spring.ftl" as spring />
<div class="container">

    <h1>Create New Vault</h1>

    <form class="form" role="form" action="" method="post">

        <div class="form-group">
            <label class="control-label">Vault Name:</label>
            <@spring.bind "vault.name" />
            <input type="text"
                   class="form-control"
                   name="${spring.status.expression}"
                   value="${spring.status.value!""}"/>
        </div>

        <div class="form-group">
            <label class="control-label">Description:</label>
            <@spring.bind "vault.description" />
            <textarea type="text"
                      class="form-control"
                      name="description"
                      rows="6" cols="60"></textarea>
        </div>

        <div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </div>

    </form>


</div>
</@layout.vaultLayout>